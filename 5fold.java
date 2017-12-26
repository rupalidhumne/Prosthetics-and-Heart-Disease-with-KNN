import java.io.*;
import java.util.*;
public class 5fold
   {
       public static void main(String[] args) throws Exception
      {
        Scanner infile = new Scanner(new File("sampledata.txt"));
        Scanner kb = new Scanner(System.in);
        ArrayList<Instance> list = new ArrayList<Instance>(); //list of instances
         ArrayList<Integer> responses=new ArrayList<Integer>(); //classification
         while(infile.hasNextLine())
         {
            String s = infile.nextLine();// skipped instances with questions marks or missing values
            if(!s.contains("?"))
               {
                  StringTokenizer st = new StringTokenizer(s,",");
                  while(st.hasMoreTokens())
                     {
                        Instance i = new Instance(Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Double.parseDouble(st.nextToken()),Integer.parseInt(st.nextToken()));       
                        list.add(i); 
                        original.add(i);
                            
                     }
               }      
         }
         int sections = list.size()/5;
         ArrayList<Instance> one = new ArrayList<Instance>();
         ArrayList<Instance> two = new ArrayList<Instance>();
         ArrayList<Instance> three = new ArrayList<Instance>();
         ArrayList<Instance> four = new ArrayList<Instance>();
         ArrayList<Instance> five = new ArrayList<Instance>();
         for(int x=0; x<sections; x++)
            {
               one.add(list.get(x));
            }   
         for(int x=0; x<sections; x++)
            {
               two.add(list.get(x));
            }
         for(int x=0; x<sections; x++)
            {
               three.add(list.get(x));
            }
         for(int x=0; x<sections; x++)
            {
               four.add(list.get(x));
            }
         for(int x=0; x<sections; x++)
            {
               five.add(list.get(x));
            }
        for(int x=0; x<5; x++)
         {
            if(x==0)
               {
                  //data point as test dummy
                  //test against 5 4 3 2 1
               }
         }
}                  
public static int nearestNeighbor(Instance newInstance, List<Instance> list)
   {
         sort(list,newInstance);      
         int kth = (int)(Math.sqrt(list.size()));      
         int[] nearestNeighbors = new int[kth];
         for(int x=0; x<nearestNeighbors.length; x++)
            {
               nearestNeighbors[x]=list.get(x).getReponse();
            }
         int classification=-1;
               double sum=0;
               for(int x=0; x<nearestNeighbors.length; x++)
                  {
                     sum+=nearestNeighbors[x];
                  }
               double length = 1.0*nearestNeighbors.length;
               classification=(int)Math.round(sum/nearestNeighbors.length);
               System.out.println(classification);
         newInstance.setResponse(classification);
         list.add(newInstance);
         original.add(newInstance);
        return classification;
   }
}     
                

