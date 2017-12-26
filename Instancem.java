import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
public class Instancem
{
   Double[][][] motion;
   int finger;
   
   public Instancem(Double[][][] m,int c )
      {
         motion=m;
         finger=c;
      }
   public Double[][] task1() //one sequence of 1 imgs
   {
     Double[][][] mat1 = getmat();
      ArrayList<Double[][]> diff = new ArrayList<Double[][]>();
      for(int z=0; z<149; z++)
      {
         Double[][] difference = new Double[640][480];
         for(int x=0; x<640; x++)
         {
            for(int y=0; y<480; y++)
            {
               difference[x][y]=mat1[z][x][y]-mat1[z+1][x][y];
            }
         }
         diff.add(difference);
      }
      Double[][] avgd = new Double[640][480];
      for(int r=0; r<640; r++)
      {
         for(int c=0; c<480; c++)
         {
            double sum = 0;
            for(int i=0; i<149; i++)
            {
               Double[][] matrix = diff.get(i);
               sum+=matrix[r][c];
            }
            double d = sum/149.0;
            double l = Math.round(d);
            avgd[r][c]=l;   
         }
      } 
      /*
      int[][] bw = new int[10][10]  
      int ri = 64; 
      int rc = 48; 
      int coeff = 1; 
      int bwr, bwc;
      for(int r=0; r<640; r++)
      {
    	  for(int c=0; c<480; c++)
    	  {
    		  bwc+=rc*k;
    		  bwr+= ri*k;
    		  for(int h=0; h<bwr; h++)
    		  {
    			  for(int u=0; u<bwc; u++)
    			  {
    				  
    			  }
    		  }
    	  }
      }
     */
      return avgd;
   } 
   /*
   public Double[][] task2()
   {
      Double[][][] mat1 = getmat();
      ArrayList<Double[][]> diff = new ArrayList<Double[][]>();
      ArrayList<Double[][]> sections = new ArrayList<Double[][]>();      
      for(int z=0; z<150; z++) 
      {
         Double[][] section = new Double[175][175];
         fill(section,mat1,z);
         sections.add(section);
      }
   
      for(int z=0; z<149; z++) 
      {
         Double[][] difference = new Double[175][175];
        Double[][] m1 = sections.get(z);
        Double[][] m2=sections.get(z+1);
         for(int x=0; x<m1.length; x++)
         {
            for(int y=0; y<m1[0].length; y++)
            {
               difference[x][y]=m1[x][y]-m2[x][y];
            }
         }
         diff.add(difference);
      }
      Double[][] avgd = new Double[175][175];
      for(int r=0; r<175; r++)
      {
         for(int c=0; c<175; c++)
         {
            double sum = 0;
            for(int i=0; i<149; i++)
            {
               Double[][] matrix = diff.get(i);
               sum+=matrix[r][c];
            }
            avgd[r][c]=sum/149;   
         }
      }   
                           
      return avgd;
   }
   */
   
  public void fill(Double[][] section, Double[][][] movement,int z)
   {  
      for(int x=232; x<407; x++) //rows, 5/2=320, 175/2=88 320+88=407
         {      
            for(int y=152; y<327; y++)
            {
               section[x-232][y-152]=movement[z][x][y];        
            }
         }
}   
   /*
   public Double[][] task3()
   {
      Double[][][] mat1 = getmat();
      ArrayList<Double[][]> diff = new ArrayList<Double[][]>();
      ArrayList<Integer> rowcor = new ArrayList<Integer>();
      ArrayList<Integer> colcor = new ArrayList<Integer>();
      for(int x=0; x<175; x++)
      {
         int r = (int)(Math.random()*639+1);
         rowcor.add(r);
         int c = (int)(Math.random()*479 +1);
         colcor.add(c);
      }   
      Double[][][]sections = new Double[1][175][175];
      //ArrayList<Double[][]> sections = new ArrayList<Double[][]>();
      for(int z=0; z<150; z++) 
      {
         Double[][] section = new Double[175][175]; //picked 1 percent of points (30720)
         for(int x=0; x<175; x++)
         {
            for(int y=0; y<175; y++)
            {
               int r = rowcor.get(x);
               int c = colcor.get(y);
               section[x][y]=mat1[z][r][c];
            }
         }
         sections[z]=section;  //create new 3d matrix of 175 by 175 by 1 which has the 10% of pts from each frame
      }               
      for(int z=0; z<149; z++) 
      {
         Double[][] difference = new Double[175][175];
         for(int x=0; x<sections.length; x++)
         {
            for(int y=0; y<sections[0].length; y++)
            {
               difference[x][y]=sections[z][x][y]-sections[z+1][x][y];
            }
         }
         diff.add(difference);
      }
      Double[][] avgd = new Double[175][175];
      for(int r=0; r<175; r++)
      {
         for(int c=0; c<175; c++)
         {
            double sum = 0;
            for(int i=0; i<149; i++)
            {
               Double[][] matrix = diff.get(i);
               sum+=matrix[r][c];
            }
            avgd[r][c]=sum/149;   
         }
      }   
                           
      return avgd;
   }
   */ 
 public Double[][][] getmat()
   {
      return motion;
   }
public int getS()
   {
      return finger;
   }         
  

}        
      
   
         
   