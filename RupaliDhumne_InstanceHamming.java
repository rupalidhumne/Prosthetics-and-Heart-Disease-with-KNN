import java.util.ArrayList;
public class RupaliDhumne_InstanceHamming
{
   Double[][][] motion;
   int action;
   /*
   static final int nRows = 3; //rows of actual matrix
   static final int nCols= 3; //actual matrix
   static final int nCoords=9;
   static final int nCoordsSize=3; //frames
   static final int nFrameDiff=1; 
   */
   static final int nRows = 480; //rows of actual matrix for task1
   static final int nCols= 640; //actual matrix for task1
   static final int nCoordsSize=175; 
   static int nFrameDiff=27; 
   
   public InstanceHamming(Double[][][] m,int c)
      {
         motion=m;
         action=c;
      }
   /*
   public Double[][] task1() //one sequence of 1 imgs
   {
     Double[][][] mat1 = getmat();
      ArrayList<Double[][]> diff = new ArrayList<Double[][]>();
      for(int z=0; z<nFrameDiff; z++)
      {
         Double[][] difference = new Double[nRows][nCols];
         for(int x=0; x<nRows; x++)
         {
            for(int y=0; y<nCols; y++)
            {
               difference[x][y]=Math.abs(mat1[z+1][x][y]-mat1[z][x][y]);
            }
         }
         diff.add(difference);
      }
      Double[][] avgd = new Double[nRows][nCols];
      for(int r=0; r<nRows; r++)
      {
         for(int c=0; c<nCols; c++)
         {
            Double sum = 0;
            for(int i=0; i<nFrameDiff; i++)
            {
               Double[][] matrix = diff.get(i);
               sum+=matrix[r][c];
            }
            avgd[r][c]=(sum/nFrameDiff);   
         }
      }   
                           
      return avgd;
   } 
   
   public Double[][] task2()
   {
      Double[][][] mat1 = getmat();
	      ArrayList<Double[][]> diff = new ArrayList<Double[][]>();

	      int z=0;
	      int actualDiffFrames=0; //x actual frames
	      while(mat1[z+1][0][0]!=null)
	      {
	    	  actualDiffFrames++;
	    	  Double[][] difference = new Double[nRows][nCols];

	         for(int x=0; x<nRows; x++)
	         {
	            for(int y=0; y<nCols; y++)
	            {
	            	//System.out.println("First: " + mat1[z+1][x][y]);
	            	//System.out.println("Second: " + mat1[z][x][y]);
	            	difference[x][y]=Math.abs(mat1[z+1][x][y]-mat1[z][x][y]);
	            }
	         }
	         diff.add(difference);
	        z++; 
	      }
	      Double[][] avgd = new Double[nRows][nCols];
	      for(int r=0; r<nRows; r++)
	      {
	         for(int c=0; c<nCols; c++)
	         {
	            double sum = 0;
	            for(int i=0; i<actualDiffFrames; i++)
	            {
	               Double[][] matrix = diff.get(i);
	               sum+=matrix[r][c];
	            }
	             avgd[r][c]=sum/actualDiffFrames;
	         }
	      }   
  
   */
    public Integer[][] task3()//add int action //only aggregate if there's a frame to aggregate
   {
      Double[][][] mat1 = getmat();
      ArrayList<Double[][]> diff = new ArrayList<Double[][]>();
     Integer[][] binarized = new Integer[nCoordsSize][nCoordsSize];

      int z=0;
      int actualDiffFrames=0; //x actual frames
      while(mat1[z+1][0][0]!=null)
      {
    	  actualDiffFrames++;
    	  Double[][] difference = new Double[nCoordsSize][nCoordsSize];

         for(int x=0; x<nCoordsSize; x++)
         {
            for(int y=0; y<nCoordsSize; y++)
            {
            	difference[x][y]=Math.abs(mat1[z+1][x][y]-mat1[z][x][y]);
            }
         }
         diff.add(difference);
        z++; 
      }
       //now we're using 0s and 1s
      Double[][] avgd = new Double[nCoordsSize][nCoordsSize];
      for(int r=0; r<nCoordsSize; r++)
      {
         for(int c=0; c<nCoordsSize; c++)
         {
            double sum =0.0;
            for(int i=0; i<actualDiffFrames; i++)
            {
               Double[][] matrix = diff.get(i);
               sum+=matrix[r][c];
            }
            avgd[r][c]=sum/actualDiffFrames;
         }
         
      }
      
      for(int x=0; x<avgd.length; x++)
               for(int y=0; y<avgd.length; y++)
                  if(avgd[x][y]<=0.5)
                  {
                     binarized[x][y]=0;
                  }
                else
                  {
                     binarized[x][y]=1;
                  }   

   
                           
      return binarized;
   }
   
 public Double[][][] getmat()
   {
      return motion;
   }
public int getS()
   {
      return action;
   }         
  public String toString2()
  {
	  	String blah="";
	  
	  		for(int x=0; x<nCoordsSize; x++)
	  		{
	  			for(int y=0; y<nCoordsSize; y++)
	  			{
	  				blah+=motion[1][x][y];
	  			}
	  		}
	  	
	  return blah;
  }

}        
      
   
         
   