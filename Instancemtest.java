import java.util.*;
public class Instancemtest
{
   Double[][][] motion;
   int finger;
   /*
   static final int nRows = 3; //rows of actual matrix
   static final int nCols= 3; //actual matrix
   static final int nCoords=9;
   static final int nCoordsSize=3; //frames
   static final int nFrameDiff=1; 
   */
   /*
   static final int nRows = 640; //rows of actual matrix for task1
   static final int nCols= 480; //actual matrix for task1
   static final int nCoordsSize=175; 
   static final int nFrameDiff=149; 
   */
   public Instancem(Double[][][] m,int c)
      {
         motion=m;
         finger=c;
      }
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
            double sum = 0;
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
   
      for(int z=0; z<nFrameDiff; z++) 
      {
         Double[][] difference = new Double[nCoordsSize][nCoordsSize];
        //Double[][] m1 = sections.get(z);
        //Double[][] m2=sections.get(z+1);
         for(int x=0; x<nCoordsSize; x++)
         {
            for(int y=0; y<nCoordsSize; y++)
            {
               difference[x][y]=Math.abs(mat1[z+1][x][y]-mat1[z][x][y]);
            }
         }
         diff.add(difference);
      }
      Double[][] avgd = new Double[nCoordsSize][nCoordsSize];
      for(int r=0; r<nCoordsSize; r++)
      {
         for(int c=0; c<nCoordsSize; c++)
         {
            double sum = 0;
            for(int i=0; i<nFrameDiff; i++)
            {
               Double[][] matrix = diff.get(i);
               sum+=matrix[r][c];
            }
             avgd[r][c]=sum/nFrameDiff;
         }
      }   
                           
      return avgd;
   }
  
   
    public Double[][] task3()
   {
      Double[][][] mat1 = getmat();
      ArrayList<Double[][]> diff = new ArrayList<Double[][]>();     
      for(int z=0; z<nFrameDiff; z++) 
      {
         Double[][] difference = new Double[nCoordsSize][nCoordsSize];
         for(int x=0; x<nCoordsSize; x++)
         {
            for(int y=0; y<nCoordsSize; y++)
            {
               difference[x][y]=Math.abs(mat1[z+1][x][y]-mat1[z][x][y]); //figure out which is null pointer through print statements
            }
         }
         diff.add(difference);
      }
      Double[][] avgd = new Double[nCoordsSize][nCoordsSize];
      for(int r=0; r<nCoordsSize; r++)
      {
         for(int c=0; c<nCoordsSize; c++)
         {
            double sum = 0;
            for(int i=0; i<nFrameDiff; i++)
            {
               Double[][] matrix = diff.get(i);
               sum+=matrix[r][c];
            }
             avgd[r][c]=sum/nFrameDiff;
         }
      }   
                           
      return avgd;
   }
   
 public Double[][][] getmat()
   {
      return motion;
   }
public int getS()
   {
      return finger;
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
      
   
         
   