import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
public class Similarity
{
    public static void main(String[] args) throws FileNotFoundException
         {
            Scanner kb = new Scanner(System.in);
            System.out.println("Enter number of motions");
            int m = kb.nextInt();
            System.out.println("Enter images per motion");
            int i = kb.nextInt();
            ArrayList<Double[][][]> motions = new ArrayList<Double[][][]>();
            for(int x=0; x<m; x++)
               {
                    Double[][][] matrix = new Double[640][480][i];
                  for(int a=0; a<i; a++)
                     {
                        String filename = "Motion"+(x+1)+ "_" + (a+1)+".txt";
                        Scanner infile = new Scanner(new File(filename));
                        for(int r=0; r<matrix.length; r++)
                           {
                              for(int c=0;c<matrix[0].length; c++)
                                 {
                                    matrix[r][c][a]=infile.nextDouble();
                                 }
                           }       
                     }
                    motions.add(matrix);
               }
             ArrayList<Double[][]> computes1 = new ArrayList<Double[][]>();
             ArrayList<Double[][]> computes2 = new ArrayList<Double[][]>();
             ArrayList<Double[][]> computes3 = new ArrayList<Double[][]>();
             for(int x=0; x<motions.size(); x++)
               {
                  Double[][] avg=task1(motions,x);
                  computes1.add(avg);
                  Double[][] avg2 = task2(motions,x);
                  computes2.add(avg2);
                  Double[][] avg3 = task2(motions,x);
                  computes3.add(avg3);
               }  
           System.out.println("The correlation for x and y through method 1 is" + correlation(computes1));
           System.out.println("The correlation for x and y through method 1 is" + correlation(computes2)); 
           System.out.println("The correlation for x and y through method 1 is" + correlation(computes3));   
               
         }
      public static double correlation(ArrayList<Double[][]> avgs)
         {
            return corr2(avgs.get(0), avgs.get(1));
         }
      public static double corr2(Double[][] a, Double[][] b)
         {
           double meanA=mean(a);
            double meanB=mean(b);
            double top = top(meanA, meanB, a, b);
            double bottom = bottom(meanA, meanB, a, b);  
            return top/bottom; 
         }
       public static double bottom(double ma, double mb, Double[][] a, Double[][] b)
         {
            double sumA=0;
            double sumB=0;
            for(int x=0; x<640; x++)
               {
                  for(int y=0; y<480; y++)
                     {
                        sumA+=Math.pow((a[x][y]-ma),2);
                     }
               } 
             for(int x=0; x<640; x++)
               {
                  for(int y=0; y<480; y++)
                     {
                        sumB+=Math.pow((a[x][y]-mb),2);
                     }
               }
           return Math.sqrt(sumA*sumB);
      }             
                        
       public static double mean(Double[][] arr)
         {
            double sum = 0;
            for(int x=0; x<arr.length; x++)
               {
                  for(int y=0; y<arr[0].length; y++)
                     {
                       sum+=arr[x][y];
                     }
               }
               return 1.0*(sum/(640 *480));
         }
         public static double top(double ma, double mb, Double[][] a, Double[][]b)
            {
               double top = 0;
               for(int m=0; m<640; m++)
                  {
                     for(int n=0; n<480; n++)
                        {
                           top+= (a[m][n]-ma)*(b[m][n]*mb);
                        }
                  }
                return top;
            }                                    
                            
      public static Double[][] task1(ArrayList<Double[][][]> motions, int motion) //one sequence of 10 imgs
         {
            Double[][][] movement = motions.get(motion);
            ArrayList<Double[][]> diff = new ArrayList<Double[][]>();
            for(int z=0; z<9; z++)
               {
                  Double[][] difference = new Double[640][480];
                  for(int x=0; x<movement.length; x++)
                     {
                        for(int y=0; y<movement[0].length; y++)
                           {
                              difference[x][y]=movement[x][y][z]-movement[x][y][z+1];
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
                        for(int i=0; i<9; i++)
                           {
                              Double[][] matrix = diff.get(i);
                              sum+=matrix[r][c];
                           }
                        avgd[r][c]=sum/10;   
                     }
                }   
                           
         return avgd;
        } 
     public static Double[][] task2(ArrayList<Double[][][]> motions, int motion)
      {
          Double[][][] movement = motions.get(motion);
          ArrayList<Double[][]> diff = new ArrayList<Double[][]>();
          Double[][][]sections = new Double[175][175][10];
               for(int z=0; z<10; z++) 
               {
                 int r=-1; 
                 int c=-1; 
                 Double[][] section = new Double[175][175];
                 for(int x=232; x<408; x++) //rows, 640/2=320, 175/2=88 320+88=407
                  {
                     r++;
                     for(int y=152; y<328; y++)
                        {
                           c++;
                           section[r][c]=movement[x][y][z];
                        }
                  }
                sections[z]=section;
               }
 
               for(int z=0; z<9; z++) 
                  {
                     Double[][] difference = new Double[175][175];
                  for(int x=0; x<sections.length; x++)
                     {
                        for(int y=0; y<sections[0].length; y++)
                           {
                              difference[x][y]=sections[x][y][z]-sections[x][y][z+1];
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
                        for(int i=0; i<9; i++)
                           {
                              Double[][] matrix = diff.get(i);
                              sum+=matrix[r][c];
                           }
                        avgd[r][c]=sum/10;   
                     }
                }   
                           
         return avgd;
      }   
    public static Double[][] task3(ArrayList<Double[][][]> motions, int motion)
      {
        Double[][][] movement = motions.get(motion);
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
        Double[][][]sections = new Double[175][175][10];
        for(int z=0; z<10; z++) 
               {
                 Double[][] section = new Double[175][175]; //picked 10 percent of points (30720)
                 for(int x=0; x<sections.length; x++)
                     {
                        for(int y=0; y<sections[0].length; y++)
                           {
                              int r = rowcor.get(x);
                              int c = colcor.get(y);
                              section[x][y]=movement[r][c][z];
                           }
                     }
                  sections[z]=section;  //create new 3d matrix of 175 by 175 by 10 which has the 10% of pts from each frame
               }               
          for(int z=0; z<9; z++) 
                  {
                     Double[][] difference = new Double[175][175];
                  for(int x=0; x<sections.length; x++)
                     {
                        for(int y=0; y<sections[0].length; y++)
                           {
                              difference[x][y]=sections[x][y][z]-sections[x][y][z+1];
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
                        for(int i=0; i<9; i++)
                           {
                              Double[][] matrix = diff.get(i);
                              sum+=matrix[r][c];
                           }
                        avgd[r][c]=sum/10;   
                     }
                }   
                           
         return avgd;
   }      
      

                     
           
         
}                        
                  
                  