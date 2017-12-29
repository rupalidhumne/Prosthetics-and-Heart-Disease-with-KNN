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
		public boolean equals(Object c)
		{
			if(c instanceof Coord)
         {
            Coord ptr = (Coord)c;
            if(ptr.getRow()==row && ptr.getCol()==col)
   			{
   				return true;
   			}
   			else 
   				return false;
         }
         else
            return false;      
		}



}
	
	
