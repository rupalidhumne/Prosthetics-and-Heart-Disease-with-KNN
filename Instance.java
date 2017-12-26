public class Instance
   {
      private int response;
      private double age;
      private double sex;
      private double cp;
      private double trestbps;
      private double chol;
      private double fbs;
      private double restecg;
      private double thalach;
      private double exange;
      private double oldpeak;
      private double slope;
      private double ca;
      private double thal; 
      
      public Instance(double a, double s, double c, double t, double ch, double f, double r, double th, double e, double o, double slp, double Ca, double thl, int resp)
         {
            
            age=a; 
            sex=s;
            cp=c;
            trestbps = t;
            chol=ch;
            f=fbs;
            r=restecg;
            thalach=th;
            e=exange;
            oldpeak=o;
            slope=slp;
            ca=Ca;
            thal=thl;
            response=resp;
         }   
       public double getAge()
         {
            return age;
         }
        public double getSex()
         {
            return sex;
         }
       public double getCp()
         {
            return cp;
         }
         public double getTrest()
         {
            return trestbps;
         }
      public double getChol()
         {
            return chol;
         }
          public double getFbs()
         {
            return fbs;
         }
      public double getRestecg()
         {
            return restecg;
         }
      public double getThalach()
         {
            return thalach;
         }
      public double getExange()
         {
            return exange;
         }
       public double getOld()
         {
            return oldpeak;
         }
       public double getSlope()
         {
            return slope;
         }
        public double getCa()
         {
            return ca;
         }
        public double getThal()
         {
            return thal;
         }
         public int getResponse()
            {
               return response;
            }
        public void setResponse(int r)
         {
            response=r;
         } 
         public double distance(Instance I)
            {
              return Math.sqrt(Math.pow((getAge()-I.getAge()),2)+Math.pow((getSex()-I.getSex()),2)+Math.pow((getCp()-I.getCp()),2)+Math.pow((getTrest()-I.getTrest()),2)+Math.pow((getChol()-I.getChol()),2)+Math.pow((getFbs()-I.getFbs()),2)+Math.pow((getRestecg()-I.getRestecg()),2)+Math.pow((getThalach()-I.getThalach()),2)+Math.pow((getExange()-I.getExange()),2)+Math.pow((getOld()-I.getOld()),2) +Math.pow((getSlope()-I.getSlope()),2)+Math.pow((getCa()-I.getCa()),2)+Math.pow((getThal()-I.getThal()),2));
            }
        public String toString()
         {
            String s=""+age+","+sex+","+cp+","+trestbps+","+chol+","+fbs+","+restecg+","+thalach+","+exange+","+oldpeak+","+slope+","+ca+","+thal+","+response;
            return s;
         }
   }      
                  
     
         
      
         