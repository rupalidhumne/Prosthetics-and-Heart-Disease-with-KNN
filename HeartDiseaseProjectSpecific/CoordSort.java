 
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
		public int getRow()
		{
			return row;
		}
		public int getCol()
		{
			return col;
		}
		public int compareTo(Coord c)
		{
			if(c.getRow()==row && c.getCol()==col)
			{
				return 0;
			}
			else 
				return -1;
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
	
	
}