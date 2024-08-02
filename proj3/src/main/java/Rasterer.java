import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    //LonDpp数组表示地图缩放深度
    private static double[] LonDPP = new double[8];
    //从MapSever中获取的最初地图的经纬度边界
    private static final double INITLRLON = MapServer.ROOT_LRLON,
    INITULLON = MapServer.ROOT_ULLON,INITLRLAT = MapServer.ROOT_LRLAT,
    INITULLAT = MapServer.ROOT_ULLAT;

    //静态变量的初始化
    static{
        LonDPP[0] = (INITLRLON - INITULLON) / MapServer.TILE_SIZE;
        for(int i = 1; i < 8; i++){
            //每当地图深度加深，LonDPP都要减半
            LonDPP[i] = LonDPP[i - 1] / 2;
        }
    }
    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //System.out.println(params);
        Map<String, Object> results = new HashMap<>();
//        System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
//                           + "your browser.");
        //获取服务器中的地图相关参数
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");

        //判断参数是否合法,不合法的话设置为0并且返回.
        if(lrlat >= INITULLAT || lrlon <= INITULLON || ullat <= INITLRLAT ||
        ullon >= INITLRLON || ullon >= lrlon || ullat <= lrlat){
            results.put("query_syccess", false);
            results.put("depth", 0);
            //表示没有要渲染的瓦片
            results.put("render_grid", null);
            results.put("raster_ul_lon", 0);
            results.put("raster_ul_lat", 0);
            results.put("raster_lr_lon", 0);
            results.put("raster_lr_lat", 0);
            return results;
        }

        //根据参数得到缩放深度
        double aimLonDPP = (lrlon - ullon) / params.get("w");
        int depth = getDepth(aimLonDPP);
        results.put("depth", depth);

        //maxLevel表示当前画面x和y方向分别由多少小地图拼接而成
        double maxLevel = Math.pow(2, depth);
        //计算出每个小地图之间的间距，便于遍历小地图
        double xDiff = (INITLRLON - INITULLON) / maxLevel;
        double yDiff = (INITLRLAT - INITULLAT) / maxLevel;

        //从边界开始遍历，找到目标区域所对应的地图下标
        //xLeft表示目标区域对应最左边图片下标，xRight表示对应目标区域最右边图片下标
        int xLeft = 0, xRight = 0, yLeft = 0, yRight = 0;
        for(double x = INITULLON; x <= INITLRLON; x+=xDiff){
            if(x <= ullon){
                xLeft++;
            }
            if(xRight < maxLevel && x <= lrlon){
                xRight++;
            }
        }
        for(double y = INITULLAT; y >= INITLRLAT; y+=yDiff){
            if(y >= ullat){
                yLeft++;
            }
            if(yRight < maxLevel && y >= lrlat){
                yRight++;
            }
        }

        //调整索引，确保索引位于正确的范围
        if(xLeft != 0){
            xLeft--;
        }
        if(xRight != 0){
            xRight--;
        }
        if(yLeft != 0){
            yLeft--;
        }
        if(yRight != 0){
            yRight--;
        }

        //按照要求，将目标图片储存到String[][]数组中
        String[][] files = new String[yRight - yLeft + 1][xRight - xLeft + 1];
        for(int y = yLeft; y <= yRight; y++){
            for(int x = xLeft; x <= xRight; x++){
                files[y - yLeft][x - xLeft] = "d" + depth + "_x" + x + "_y" + y + ".png";
            }
        }

        //根据缩放深度的到当前的经纬度
        results.put("render_grid", files);
        results.put("raster_ul_lon", INITULLON + xLeft * xDiff);
        results.put("raster_ul_lat", INITULLAT + yLeft * yDiff);
        results.put("raster_lr_lon", INITULLON + (xRight + 1) * xDiff);
        results.put("raster_lr_lat", INITULLAT + (yRight + 1) * yDiff);
        results.put("query_success", true);

        //System.out.println(results);

       return results;
    }

    //得到当前LonDPP对应的图像深度（也可以说原图片缩放程度）
    private int getDepth(double aimLonDPP){
        int depth = 0;
        while(aimLonDPP < LonDPP[depth]){
            depth++;
            if(depth == LonDPP.length - 1){
                break;
            }
        }
        return depth;
    }

}
