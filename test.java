
public class test 
{
	 public static void main(String[] args) throws Exception //if still OOM subtract as you bring it in
	 {
		 Double[][] a = new Double[48][64];
		 Double[][] b = new Double[48][64];
		 for(int r=0; r<a.length; r++)
		 {
			 for(int c=0; c<a[0].length; c++)
			 {
				 a[r][c]=Math.random() *20;
				 b[r][c]=Math.random() *20;
				 double start = System.nanoTime();
				 corr2(a,b);
				 double end = System.nanoTime();
				 System.out.println("time:" +(end-start)/1000000000);
			 }
		 }
	 }



  public static double corr2(Double[][] a,Double[][] b)
  {
     double meanA=mean(a);
     double meanB=mean(b);
     double top = top(meanA, meanB, a, b);
     double bottom = bottom(meanA, meanB, a, b);  
     return Math.abs(top/bottom); 
  }
  public static double bottom(double ma, double mb, Double[][] a, Double[][] b)
  {
     double sumA=0;
     double sumB=0;
     for(int x=0; x<a.length; x++)
     {
        for(int y=0; y<a[0].length; y++)
        {
           sumA+=Math.pow((a[x][y]-ma),2);
           sumB+=Math.pow((b[x][y]-mb),2);
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
     return 1.0*(sum/(arr.length *arr[0].length));
  }
  public static double top(double ma, double mb, Double[][] a, Double[][]b)
  {
     double top = 0;
     for(int m=0; m<a.length; m++)
     {
        for(int n=0; n<a[0].length; n++)
        {
           top+= (a[m][n]-ma)*(b[m][n]-mb);
        }
     }
     return top;
  }  

}
