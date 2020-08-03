import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

class Percolation {
    private WeightedQuickUnionUF grid;
    private WeightedQuickUnionUF full;
    private int n;
    private int top;
    private int bottom;
    private boolean[][] openNodes;
    private int openSites = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int s){
        n = s;
        if (n <= 0) {
            throw new java.lang.IllegalArgumentException("n, must be grater than zero.\n");
        }
        grid = new WeightedQuickUnionUF(n*n + 2); /// virtual top+bottom
        full = new WeightedQuickUnionUF(n*n + 1); /// virtual top
        openNodes = new boolean[n][n];
        top = n*n;
        bottom = n*n+1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        if(!isValid(row, col)) {
            throw new java.lang.IndexOutOfBoundsException("Index is out of bound");
//            return;
        }
        openSites++;
        if(isOpen(row, col)){
            return;
        }

        openNodes[row-1][col-1] = true;
        int flatIndex = flattenGrid(row, col) - 1;
        if (row == 1 || col == 1) {
            grid.union(top, flatIndex);
            full.union(top, flatIndex);
//            return;
        }

        else if(row == n || col == n){
            grid.union(bottom, flatIndex);
//            return;
        }

        if(isValid(row, col - 1) && isOpen(row, col - 1)){
            grid.union(flatIndex, flattenGrid(row, col - 1)-1);
            full.union(flatIndex, flattenGrid(row, col - 1)-1);
        }

        if(isValid(row, col + 1) && isOpen(row, col + 1)){
            grid.union(flatIndex, flattenGrid(row, col + 1)-1);
            full.union(flatIndex, flattenGrid(row, col + 1)-1);
        }

        if(isValid(row - 1, col) && isOpen(row - 1, col)){
            grid.union(flatIndex, flattenGrid(row - 1, col)-1);
            full.union(flatIndex, flattenGrid(row - 1, col)-1);
        }

        if(isValid(row + 1, col) && isOpen(row + 1, col)){
            grid.union(flatIndex, flattenGrid(row + 1, col)-1);
            full.union(flatIndex, flattenGrid(row + 1, col)-1);
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
//        if (!isValid(row, col)){
//            throw new java.lang.IndexOutOfBoundsException("Index is out of bound open");
//        }
//        StdOut.print(row);
//        StdOut.print(col);
        return openNodes[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        int idx = flattenGrid(row, col);
        return full.connected(idx, top);
    }

    public boolean isValid(int row,int col){
        int shiftRow = row - 1;
        int shiftCol = col - 1;
        return (shiftRow >= 0 && shiftCol >= 0 && shiftRow < n && shiftCol < n);
//        return i - 1 >= 0 && j - 1 >= 0
//                && i < n && j < n;
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return  openSites;
    }

    private int flattenGrid(int row, int col) {
        return n * (row - 1) + col;
    }

    // does the system percolate?
    public boolean percolates() {
        return grid.connected(top , bottom);
    }

    // test client (optional)
    public static void main(String[] args){
        int size = Integer.parseInt(args[0]);

        Percolation percolation = new Percolation(size);
        int argCount = args.length;
        for (int i = 1; argCount >= 2; i += 2) {
            int row = Integer.parseInt(args[i]);
            int col = Integer.parseInt(args[i + 1]);
            StdOut.printf("Adding row: %d  col: %d %n", row, col);
            percolation.open(row, col);
            if (percolation.percolates()) {
                StdOut.printf("The System percolates\n");
            }
            argCount -= 2;
        }
//        StdOut.print(percolation.numberOfOpenSites());
        if (!percolation.percolates()) {
            StdOut.print("Does not percolate\n");
        }

    }
}