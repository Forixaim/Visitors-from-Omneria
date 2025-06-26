package net.forixaim.omneria.util;

import net.minecraft.world.phys.Vec3;

import java.util.*;

public class Pathing {
    List<Vec3> findPath(Vec3 start, Vec3 goal, Set<Vec3> obstacles, double maxX, double maxY, double maxZ) {
        PriorityQueue<AStarVecNode> open = new PriorityQueue<>(Comparator.comparingDouble(AStarVecNode::f));
        Map<Vec3, AStarVecNode> allNodes = new HashMap<>();
        AStarVecNode startNode = new AStarVecNode(start, 0, heuristic(start, goal), null);
        open.add(startNode);
        allNodes.put(start, startNode);
        Set<Vec3> closed = new HashSet<>();

        int[][] dirs = {
                {1,0,0},{-1,0,0},{0,1,0},{0,-1,0},{0,0,1},{0,0,-1},
                {1,1,0}, {1,-1,0}, {-1,1,0}, {-1,-1,0},
                {1,0,1}, {1,0,-1}, {-1,0,1}, {-1,0,-1},
                {0,1,1}, {0,1,-1}, {0,-1,1}, {0,-1,-1}
        };

        while (!open.isEmpty()) {
            AStarVecNode curr = open.poll();
            if (curr.pos.equals(goal)) return buildPath(curr);

            closed.add(curr.pos);

            for (int[] d : dirs) {
                Vec3 neighborPos = curr.pos.add(d[0], d[1], d[2]);
                if (neighborPos.x < 0 || neighborPos.x > maxX ||
                        neighborPos.y < 0 || neighborPos.y > maxY ||
                        neighborPos.z < 0 || neighborPos.z > maxZ ||
                        obstacles.contains(neighborPos)) continue;

                double tentativeG = curr.g + cost(curr.pos, neighborPos);
                AStarVecNode neighbor = allNodes.getOrDefault(neighborPos, new AStarVecNode(neighborPos, Double.POSITIVE_INFINITY, heuristic(neighborPos, goal), null));
                if (tentativeG < neighbor.g) {
                    neighbor.g = tentativeG;
                    neighbor.parent = curr;
                    neighbor.h = heuristic(neighborPos, goal);
                    allNodes.put(neighborPos, neighbor);
                    if (!closed.contains(neighborPos)) open.add(neighbor);
                }
            }
        }
        return Collections.emptyList(); // no path
    }

    double heuristic(Vec3 a, Vec3 b) {
        return a.distanceTo(b);
    }

    double cost(Vec3 from, Vec3 to) {
        return from.distanceTo(to);
    }

    List<Vec3> buildPath(AStarVecNode node) {
        List<Vec3> path = new ArrayList<>();
        for (AStarVecNode n = node; n != null; n = n.parent) path.add(n.pos);
        Collections.reverse(path);
        return path;
    }

    public static class AStarVecNode {
        Vec3 pos;
        double g, h;
        AStarVecNode parent;

        AStarVecNode(Vec3 pos, double g, double h, AStarVecNode parent) {
            this.pos = pos;
            this.g = g;
            this.h = h;
            this.parent = parent;
        }

        double f() { return g + h; }

        // Essential for Sets/Maps
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof AStarVecNode)) return false;
            Vec3 p = ((AStarVecNode)o).pos;
            return pos.equals(p);
        }

        @Override
        public int hashCode() {
            return pos.hashCode();
        }
    }
}
