package creatures;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import huglife.Action;
import huglife.Creature;
import huglife.Direction;
import huglife.HugLifeUtils;
import huglife.Occupant;

public class Clorus extends Creature {
    private int r;
    private int g;
    private int b;
    private static final double moveEnergyLost = 0.03;
    private static final double stayEnergyLost = 0.01;

    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    public Clorus() {
        this(1);
    }

    @Override
    public Color color() {
        return color(r, g, b);
    }

    @Override
    public void attack(Creature c) {
        energy += c.energy();
    }

    @Override
    public void move() {
        energy -= moveEnergyLost;
    }

    @Override
    public Clorus replicate() {
        energy /= 2;
        return new Clorus(energy);
    }

    @Override
    public void stay() {
        energy -= stayEnergyLost;
    }

    @Override
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empty = getNeighborsOfType(neighbors, "empty");
        List<Direction> plip = getNeighborsOfType(neighbors, "plip");

        if (empty.size() == 0) {
            return new Action(Action.ActionType.STAY);
        } else if (!plip.isEmpty()) {
            Direction move = HugLifeUtils.randomEntry(plip);
            return new Action(Action.ActionType.ATTACK, move);
        } else if (energy >= 1) {
            Direction move = HugLifeUtils.randomEntry(empty);
            return new Action(Action.ActionType.REPLICATE, move);
        } else {
            Direction move = HugLifeUtils.randomEntry(empty);
            return new Action(Action.ActionType.MOVE, move);
        }
    }

}
