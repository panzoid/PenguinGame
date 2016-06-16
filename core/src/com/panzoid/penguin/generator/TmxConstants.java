package com.panzoid.penguin.generator;

/**
 * Created by weipa on 6/14/16.
 */
public class TmxConstants {

    public static final String TMX_FILE = "%s.%s.%s.tmx"; //(level_number).(minimum_moves).(maximum_moves).tmx

    public static final int TMX_HEIGHT = 15;
    public static final int TMX_WIDTH = 15;

    public static final String TMX_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><map version=\"1.0\" orientation=\"orthogonal\" renderorder=\"right-down\" width=\"%s\" height=\"%s\" tilewidth=\"16\" tileheight=\"16\" nextobjectid=\"1\"> <tileset firstgid=\"1\" name=\"Ice\" tilewidth=\"16\" tileheight=\"16\" tilecount=\"10\" columns=\"0\">  <tile id=\"0\">   <image width=\"16\" height=\"16\" source=\"../tilesheets/ice.png\"/>  </tile>  <tile id=\"1\">   <properties>    <property name=\"flag\" type=\"int\" value=\"4\"/>   </properties>   <image width=\"16\" height=\"16\" source=\"../tilesheets/ice_rock.png\"/>  </tile>  <tile id=\"2\">   <properties>    <property name=\"flag\" type=\"int\" value=\"1\"/>   </properties>   <image width=\"16\" height=\"16\" source=\"../tilesheets/ice_thin.png\"/>  </tile>  <tile id=\"3\">   <image width=\"16\" height=\"16\" source=\"../tilesheets/ice_crack.png\"/>  </tile>  <tile id=\"4\">   <image width=\"16\" height=\"16\" source=\"../tilesheets/ice_hole.png\"/>  </tile>  <tile id=\"5\">   <properties>    <property name=\"flag\" type=\"int\" value=\"2\"/>   </properties>   <image width=\"16\" height=\"16\" source=\"../tilesheets/ladder_down.png\"/>  </tile>  <tile id=\"6\">   <properties>    <property name=\"flag\" type=\"int\" value=\"8\"/>   </properties>   <image width=\"16\" height=\"16\" source=\"../tilesheets/ice_left.png\"/>  </tile>  <tile id=\"7\">   <properties>    <property name=\"flag\" type=\"int\" value=\"16\"/>   </properties>   <image width=\"16\" height=\"16\" source=\"../tilesheets/ice_right.png\"/>  </tile>  <tile id=\"8\">   <properties>    <property name=\"flag\" type=\"int\" value=\"32\"/>   </properties>   <image width=\"16\" height=\"16\" source=\"../tilesheets/ice_up.png\"/>  </tile>  <tile id=\"9\">   <properties>    <property name=\"flag\" type=\"int\" value=\"64\"/>   </properties>   <image width=\"16\" height=\"16\" source=\"../tilesheets/ice_down.png\"/>  </tile> </tileset> <layer name=\"Tile Layer 1\" width=\"%s\" height=\"%s\">  <data encoding=\"csv\">";
    public static final String TMX_FOOTER = "</data> </layer></map>";

    public static final Vector2[] DIRECTIONS = new Vector2[]{
            new Vector2(-1, 0), //LEFT
            new Vector2(1, 0), //RIGHT
            new Vector2(0, 1), //DOWN
            new Vector2(0, -1)}; //UP

    public static final int TILE_DEFAULT = 1;
    public static final int TILE_BLOCK = 2;
    public static final int TILE_START = 3;
    //public static final int TILE_CRACK = 4;
    //public static final int TILE_HOLE = 5;
    public static final int TILE_FINISH = 6;
    public static final int TILE_LEFT = 7;
    public static final int TILE_RIGHT = 8;
    public static final int TILE_UP = 9;
    public static final int TILE_DOWN = 10;

    public static class Vector2 {
        public int x;
        public int y;

        public Vector2(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public Vector2 cpy() {
            return new Vector2(x, y);
        }

        public void scl(int xScl, int yScl) {
            x *= xScl;
            y *= yScl;
        }

        public Vector2 add(Vector2 other) {
            x += other.x;
            y += other.y;
            return this;
        }

        public Vector2 subtract(Vector2 other) {
            x -= other.x;
            y -= other.y;
            return this;
        }

        public boolean hasSameDirection(Vector2 other) {
            return x * other.x + y * other.y > 0;
        }

        public boolean hasOppositeDirection(Vector2 other) {
            return x * other.x + y * other.y < 0;
        }

        @Override
        public String toString() {
            return "Vector2(" + x + "," + y + ")";
        }

        @Override
        public boolean equals(Object other) {
            if(other instanceof Vector2) {
                return x == ((Vector2) other).x && y == ((Vector2) other).y;
            }
            return false;
        }
    }
}
