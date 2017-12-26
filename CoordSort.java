import java.util.*; 

public class CoordSort
{
	public class Coord
	{
		int row, col;
		
		public Coord ()
		{
			row = -1;
			col = -1;
		}

		public Coord (int rowIn, int colIn)
		{
			row = rowIn;
			col = colIn;
		}
	}
	
	public static void print(Coord[] coordArray, int nCoords)
	{
		// prints coordinate array by row

	    for(int i=0; i<nCoords;)
	    {
	        int currentRow = coordArray[i].row;

	        int nCols=1;

	        for(int j=i+1; j<nCoords; j++)

	            if( coordArray[j].row == currentRow ) nCols++;
	            else break;
	        
			System.out.println();
			System.out.printf("row %d:\t", currentRow);

	        for(int j=i; j < i+nCols; j++)
	        {   
	        	System.out.printf("%d ", coordArray[j].col);
	        	
	        	if( coordArray[j].row != currentRow ) // check for error
	        	{
	    			System.out.println();
	    			System.out.printf("ERROR: coordArray[%d].row %d != currentRow %d", j, coordArray[j].row, currentRow);
	    			System.out.println();
	        	}
	        }
	        
	        i += nCols;
	    }
	}
	
	public static void sort(Coord[] coordArray, int nCoords)
	{
		// sorts coordinate array by rows and columns
		
	    Coord temp;
	    
	    // sort array elements by row

	    for(int i=0; i<nCoords-1; i++ )

	        for(int j=0; j<nCoords-1-i; j++)

	            if( coordArray[j].row > coordArray[j+1].row ) // if true, swap elements
	            {
	                temp = coordArray[j];
	                coordArray[j] = coordArray[j+1];
	                coordArray[j+1] = temp;
	            }

	    // for each row, sort array elements by column

	    for(int i=0; i<nCoords-1;)
	    {
	        int currentRow = coordArray[i].row;

	        int nCols=1;

	        for(int j=i+1; j<nCoords; j++)

	            if( coordArray[j].row == currentRow ) nCols++;
	            else break;

	        for(int j=i, n=0; j < i+nCols-1; j++, n++)
	        	
		        for(int k=i; k < i+nCols-1-n; k++)

		        	if( coordArray[k].col > coordArray[k+1].col )  // if true, swap elements
		        	{
		        		temp = coordArray[k];
		        		coordArray[k] = coordArray[k+1];
		        		coordArray[k+1] = temp;
		        	} 
	        
	        i += nCols;
	    }
		
	}
	
	public static void main(String[] args)
	{
		CoordSort cs = new CoordSort();

		int nCoords = 20;
		
		Coord coordArray[] = new Coord[nCoords];
		
		Random rand = new Random(); 
		
	    for(int i=0; i<nCoords; i++)

	        coordArray[i] = cs.new Coord(rand.nextInt(4), rand.nextInt(4));
	    
	    print(coordArray, nCoords);
	
	    sort(coordArray, nCoords);

		System.out.println();
		
		print(coordArray, nCoords);
	}
}