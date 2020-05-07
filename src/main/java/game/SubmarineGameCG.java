package game;

/**
 * @author nanoo - created : 07/05/2020 - 17:56
 */
public class SubmarineGameCG {

    public static void mainLike(String[] args) {
        /* Warriors and submarines initialization */
        Warrior hero = new Warrior();
        hero.setSubmarine(new Submarine(0,0,0,0));
        Warrior opponent = new Warrior();
        opponent.setSubmarine(new Submarine(0,0,0,0));

        /* Game input */
        Scanner in = new Scanner(System.in);

        /* Map initialization */
        Map map = new Map(in.nextInt(),in.nextInt());
        map.initializeCells();

        /* Players initialization */
        hero.setId(in.nextInt());
        opponent.setDefaultId(hero.getId());

        /* Go next line for map contains */
        if (in.hasNextLine()) {
            in.nextLine();
        }

        /* Map cells initialization */
        for (int i = 0; i < Map.getHeight(); i++) {
            char[] line = in.nextLine().toCharArray();
            for (int j = 0; j < line.length; j++){
                map.getCells()[i][j] = line[j];
            }
            /* Map info display == DEBUG == */
            System.err.println(map.getCells()[i]);
        }

        /* ==== BEGIN GAME OUTPUT ==== */
        /* Hero's submarine deployment */
        System.out.println(hero.deploy(map));

        /* Game loop */
        while (true) {
            /* Turn input */
            hero.setPosition(new Position(in.nextInt(),in.nextInt()));
            hero.setLife(in.nextInt());
            if (opponent.getApproximatePosition() == null)
                opponent.setApproximatePosition(new ApproximatePosition(-1,-1,-1,-1));
            int opponentLifeBeforeTurn = opponent.getLife();
            opponent.setLife(in.nextInt());
            opponent.setDamage(opponentLifeBeforeTurn - opponent.getLife());
            hero.getSubmarine().setTorpedoCD(in.nextInt());
            System.err.println("torpedo CD : " + hero.getSubmarine().getTorpedoCD()); // DEBUG
            hero.getSubmarine().setSonar(in.nextInt());
            hero.getSubmarine().setSilence(in.nextInt());
            hero.getSubmarine().setMine(in.nextInt());
            String sonarResult = in.next();
            if (in.hasNextLine()) {
                in.nextLine();
            }
            String opponentOrders = in.nextLine();

            /* ========= TURN INITIALIZE ======== */
            StringBuilder action = new StringBuilder();
            map.updateVisitedCells(hero.position);
            System.err.println("sonar result : " + opponentOrders);
            opponent.submarine.getApproximatePositionFromSonarResult(opponentOrders,opponent,hero);
            /* opponent info display == DEBUG == */
            /*System.err.println(opponent.approximatePosition);*/
            /* Map info display == DEBUG == */
            /*for (int i = 0; i < Map.getHeight(); i++){
                System.err.println(map.getCells()[i]); TODO get back map display
            }*/

            /* =========== GAME LOGIC =========== */
            /* hero attack when possible */
            action.append(hero.attack(opponent));

            /* hero displacement */
            action.append(hero.move(map,opponent));
            /* hero charge torpedo */
            action.append(hero.submarine.chargeTorpedo());

            /* ========= TURN GAME OUTPUT ======= */
            System.out.println(action);
        }
    }
}

class Warrior {

    /* Moves */
    public static final String NORTH = "MOVE N ";
    public static final String SOUTH = "MOVE S ";
    public static final String EAST = "MOVE E ";
    public static final String WEST = "MOVE W ";

    int id;
    int life;
    int damage;
    int sector;
    Position lastTarget;
    Position position;
    ApproximatePosition approximatePosition;
    Submarine submarine;

    /* === Constructor === */

    public Warrior() {
    }

    public Warrior(int id, int life, int sector, Position position, ApproximatePosition approximatePosition, Submarine submarine) {
        this.id = id;
        this.life = life;
        this.sector = sector;
        this.position = position;
        this.approximatePosition = approximatePosition;
        this.submarine = submarine;
    }

    /* === Getters / Setters === */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getSector() {
        return sector;
    }

    public void setSector(int sector) {
        approximatePosition.defineFromSector(sector);
        this.sector = sector;
    }

    public Position getLastTarget() {
        return lastTarget;
    }

    public void setLastTarget(Position lastTarget) {
        this.lastTarget = lastTarget;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public ApproximatePosition getApproximatePosition() {
        return approximatePosition;
    }

    public void setApproximatePosition(ApproximatePosition approximatePosition) {
        this.approximatePosition = approximatePosition;
    }

    public Submarine getSubmarine() {
        return submarine;
    }

    public void setSubmarine(Submarine submarine) {
        this.submarine = submarine;
    }

    public void setDefaultId (int opponentId){
        if (opponentId == 0)
            this.setId(1);
        else
            this.setId(0);
    }

    /* === Methods === */

    public String deploy(Map map){
        Position position = map.getRandomPosition();
        return position.x + " " + position.y;
    }

    public String move(Map map, Warrior opponent){
        /* Opponent Hunting pattern :@ */
        String direction =
                map.defineBestDirection(opponent.approximatePosition.getApproximatePositionAverage(), this.getPosition());

        if (direction.trim().length() == 0)
            return this.submarine.reachSurface(map);
        else
            return direction;
    }

    public String attack(Warrior opponent) {
        Position targetPossibility = opponentWithinRange(opponent);
        if (targetPossibility != null && this.getSubmarine().getTorpedoCD() == 0){
            this.setLastTarget(targetPossibility);
            return this.submarine.launchTorpedo(targetPossibility);
        }else {
            System.err.println("Attack pas possible !!!");
            return "";
        }
    }

    public Position opponentWithinRange(Warrior opponent) {
        Position target = opponent.getPosition() == null ?
                opponent.approximatePosition.getApproximatePositionAverage() : opponent.getPosition();
        if (abs(this.position.x - target.x) <= 4 && abs(this.position.y - target.y) <= 4)
            return target;
        else
            return null;
    }

}

class Submarine{

    /* Submarines  */
    public static final String TORPEDO = "TORPEDO ";
    public static final String SURFACE = "SURFACE ";

    int torpedoCD;
    int sonar;
    int silence;
    int mine;

    /* === Constructor === */

    public Submarine() {
    }

    public Submarine(int torpedoCD, int sonar, int silence, int mine) {
        this.torpedoCD = torpedoCD;
        this.sonar = sonar;
        this.silence = silence;
        this.mine = mine;
    }

    /* === Getters / Setters === */

    public int getTorpedoCD() {
        return torpedoCD;
    }

    public void setTorpedoCD(int torpedoCD) {
        this.torpedoCD = torpedoCD;
    }

    public int getSonar() {
        return sonar;
    }

    public void setSonar(int sonar) {
        this.sonar = sonar;
    }

    public int getSilence() {
        return silence;
    }

    public void setSilence(int silence) {
        this.silence = silence;
    }

    public int getMine() {
        return mine;
    }

    public void setMine(int mine) {
        this.mine = mine;
    }

    /* === Methods === */

    public String chargeTorpedo() {
        return TORPEDO;
    }

    public String launchTorpedo(Position position){
        return TORPEDO + position.x + " " + position.y + "|";
    }

    public String reachSurface(Map map){
        map.clearVisitedCells();
        return SURFACE;
    }

    public void getApproximatePositionFromSonarResult(String result, Warrior opponent, Warrior hero){
        String[] results = result.split("[ |]");
        String moveDirection = "";
        for (int i = 0; i < results.length; i++){
            /* if opponent reach surface, get his sector */
            if (results[i].equalsIgnoreCase("SURFACE")){
                opponent.setSector(Integer.parseInt(results[i + 1]));
            }
            if (results[i].equalsIgnoreCase("TORPEDO")){
                if (results.length - i > 1 && results[i + 1].matches("^[0-9]*$")){
                    int x = Integer.parseInt(results[i + 1]);
                    int y = Integer.parseInt(results[i + 2]);
                    opponent.approximatePosition.extrapolateFromTorpedoLaunch(new Position(x,y));
                }
            }
            if (results[i].equalsIgnoreCase("MOVE")){
                moveDirection = results[i + 1];
            }
        }
        if (opponent.getDamage() != 0 && hero.getLastTarget() != null) {
            System.err.println("opponent hit : " + opponent.getDamage());
            opponent.approximatePosition.affinePositionFromLastTorpedoLaunch(opponent.getDamage(), hero.getLastTarget());
        }
        if (moveDirection.length() > 0)
            opponent.approximatePosition.updatePosition(moveDirection);
        if (opponent.getApproximatePosition().getxMin() == opponent.getApproximatePosition().getxMax()
                && opponent.getApproximatePosition().getyMin() == opponent.getApproximatePosition().getyMax()
                && opponent.getApproximatePosition().getxMin() != -1 ) {
            opponent.setPosition(new Position(opponent.getApproximatePosition().getxMin(), opponent.getApproximatePosition().getyMin()));
            System.err.println("opponent position found : " + opponent.getPosition()); // DEBUG
        }

        /* DEBUG */
        System.err.println("my position : " + hero.getPosition());
        System.err.println("his position : " + opponent.getApproximatePosition().getApproximatePositionAverage());

    }
}

class Map{

    private static int width;
    private static int height;
    private char[][] cells;

    /* === Constructor === */

    public Map() {
    }

    public Map(int width, int height) {
        Map.width = width;
        Map.height = height;
    }

    /* === Getters / Setters === */

    public static int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        Map.width = width;
    }

    public static int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        Map.height = height;
    }

    public char[][] getCells() {
        return cells;
    }

    public void setCells(char[][] cells) {
        this.cells = cells;
    }

    /* === Methods === */

    public void initializeCells(){
        if (width != 0 && height != 0)
            this.cells = new char[height][width];
    }

    public Position getRandomPosition(){
        int x;
        int y;

        do{
            x = (int) (Math.random() * width); // use to generate between 0 and map width !!
            y = (int) (Math.random() * height); // use to generate between 0 and map height !!
        }while (this.cells[y][x] == 'x');

        return new Position(x,y);
    }

    public void updateVisitedCells(Position position){
        this.cells[position.y][position.x] = 'o';
    }

    public void clearVisitedCells(){
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++){
                if (this.cells[i][j] == 'o')
                    this.cells[i][j] = '.';
            }
        }
    }

    public String checkSurroundings(Position position) {
        StringBuilder available = new StringBuilder();
        if (position.y != 0 && cells[position.y - 1][position.x] == '.')
            available.append("N");
        if (position.y != height - 1 && cells[position.y + 1][position.x] == '.')
            available.append("S");
        if (position.x != width - 1 && cells[position.y][position.x + 1] == '.')
            available.append("E");
        if (position.x != 0 && cells[position.y][position.x - 1] == '.')
            available.append("W");
        return available.toString();
    }

    public String defineBestDirection(Position opponentPosition , Position heroPosition) {
        String possibilities = this.checkSurroundings(heroPosition);
        StringBuilder direction = new StringBuilder("MOVE ");
        int deltaX;
        int deltaY;
        String directionX;
        String directionY;

        if (opponentPosition != null) {
            deltaX = abs(heroPosition.getX() - opponentPosition.getX());
            deltaY = abs(heroPosition.getY() - opponentPosition.getY());
            directionX = heroPosition.getX() - opponentPosition.getX() > 0 ? "W" : "E";
            directionY = heroPosition.getY() - opponentPosition.getY() > 0 ? "N" : "S";
        }else {
            deltaX = heroPosition.getX();
            deltaY = heroPosition.getY();
            directionX = deltaX >= Map.getWidth() / 2 ? "W" : "E";
            directionY = deltaY >= Map.getHeight() / 2 ? "N" : "S";
        }

        if (deltaY >= deltaX && possibilities.contains(directionY)) {  // TODO get land cells to define best direction instead
            direction.append(directionY);
            possibilities.replace(directionY, "");
        } else if (deltaY < deltaX && possibilities.contains(directionX)) {
            direction.append(directionX);
            possibilities.replace(directionX, "");
        } else if (possibilities.length() > 0) {
            return direction.append(possibilities, 0, 1).append(" ").toString();
        } else {
            return "";
        }

        return direction.append(" ").toString();
    }
}

class Position {

    int x;
    int y;

    /* === Constructor === */

    public Position() {
    }

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /* === Getters / Setters === */

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    /* === toString === */

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}

class ApproximatePosition{

    int xMin;
    int xMax;
    int yMin;
    int yMax;

    /* === Constructor === */

    public ApproximatePosition() {
    }

    public ApproximatePosition(int xMin, int xMax, int yMin, int yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    /* === Getters / Setters === */

    public int getxMin() {
        return xMin;
    }

    public void setxMin(int xMin) {
        this.xMin = xMin;
    }

    public int getxMax() {
        return xMax;
    }

    public void setxMax(int xMax) {
        this.xMax = xMax;
    }

    public int getyMin() {
        return yMin;
    }

    public void setyMin(int yMin) {
        this.yMin = yMin;
    }

    public int getyMax() {
        return yMax;
    }

    public void setyMax(int yMax) {
        this.yMax = yMax;
    }

    /* === toString === */

    @Override
    public String toString() {
        return "ApproximatePosition{" +
                "xMin=" + xMin +
                ", xMax=" + xMax +
                ", yMin=" + yMin +
                ", yMax=" + yMax +
                '}';
    }

    /* === Methods === */

    public void defineFromSector(int sector){
        System.err.println("define from sector : " + sector);
        ApproximatePosition newApproximatePosition = new ApproximatePosition();
        /* define x */
        if (sector == 1 || sector == 4 || sector == 7){
            newApproximatePosition.setxMin(0);
            newApproximatePosition.setxMax(Map.getWidth() / 3 - 1); ;
        }
        if (sector == 2 || sector == 5 || sector == 8){
            newApproximatePosition.setxMin(Map.getWidth() / 3);
            newApproximatePosition.setxMax(Map.getWidth() / 3 * 2 - 1);
        }
        if (sector == 3 || sector == 6 || sector == 9){
            newApproximatePosition.setxMin(Map.getWidth() / 3 * 2);
            newApproximatePosition.setxMax(Map.getWidth() - 1);
        }

        /* define y */
        if (sector == 1 || sector == 2 || sector == 3){
            newApproximatePosition.setyMin(0);
            newApproximatePosition.setyMax(Map.getHeight() / 3 - 1);
        }
        if (sector == 4 || sector == 5 || sector == 6){
            newApproximatePosition.setyMin(Map.getHeight() / 3);
            newApproximatePosition.setyMax(Map.getHeight() / 3 * 2 - 1);
        }
        if (sector == 7 || sector == 8 || sector == 9){
            newApproximatePosition.setyMin(Map.getHeight() / 3 * 2);
            newApproximatePosition.setyMax(Map.getHeight() - 1);
        }
        affinePosition(newApproximatePosition);
    }

    public void extrapolateFromTorpedoLaunch(Position position) {
        ApproximatePosition approximatePosition = new ApproximatePosition();
        approximatePosition.setxMin(position.x - 4);
        approximatePosition.setxMax(position.x + 4);
        approximatePosition.setyMin(position.y - 4);
        approximatePosition.setyMax(position.y + 4);
        affinePosition(approximatePosition);
    }

    public void affinePosition(ApproximatePosition newApproximatePosition){
        System.err.println("entry affine : " + this.toString());
        if (newApproximatePosition.xMin > this.xMin || this.getxMin() == -1)
            this.xMin = newApproximatePosition.xMin;
        if (newApproximatePosition.xMax < this.xMax || this.getxMax() == -1)
            this.xMax = newApproximatePosition.xMax;
        if (newApproximatePosition.yMin > this.yMin || this.getyMin() == -1)
            this.yMin = newApproximatePosition.yMin;
        if (newApproximatePosition.yMax < this.yMax || this.getyMax() == -1)
            this.yMax = newApproximatePosition.yMax;
        // TODO affine with land cells in map !!
        System.err.println("out affine : " + this.toString());
    }

    public void updatePosition(String direction) {
        System.err.println("entry update : " + this.toString());
        System.err.println("direction : " + direction);
        switch (direction){
            case "N":
                this.yMin --;
                this.yMax --;
                break;
            case "S":
                this.yMin ++;
                this.yMax ++;
                break;
            case "E":
                this.xMin ++;
                this.xMax ++;
                break;
            case "W":
                this.xMin --;
                this.xMax --;
                break;
        }
        System.err.println("out update : " + this.toString());
    }

    public Position getApproximatePositionAverage() {
        return new Position((this.xMax + this.xMin) / 2, (this.yMax + this.yMin) / 2);
    }

    public void affinePositionFromLastTorpedoLaunch(int damage, Position lastTarget) {
        System.err.println("torpedo launch : " + lastTarget); // DEBUG
        switch (damage){
            case 1:
                ApproximatePosition newApproximatePosition = new ApproximatePosition();
                newApproximatePosition.setxMin(lastTarget.x - 1);
                newApproximatePosition.setxMax(lastTarget.x + 1);
                newApproximatePosition.setyMin(lastTarget.y - 1);
                newApproximatePosition.setyMin(lastTarget.y + 1);
                System.err.println("go to affinePosition with : " + newApproximatePosition);
                affinePosition(newApproximatePosition);
                break;
            case 2:
                this.setxMin(lastTarget.x);
                this.setxMax(lastTarget.x);
                this.setyMin(lastTarget.y);
                this.setyMax(lastTarget.y);
                System.err.println("opponent hit ! approximatePosition : " + this.toString());
                break;
        }
    }

}
