/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.project;

/**
 *
 * @author MAHE
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.Random;
public class Generate {
        public ArrayList week = new ArrayList();
        public static ArrayList<String> subjects = new ArrayList();
        public static ArrayList<String> teachers = new ArrayList();                
        public static Map<String,Integer> credits = new HashMap();
        public static ArrayList<String> labs = new ArrayList();
        //Full days need to be fetched from DB
        public int fdays = 3;
        
        public static final String CONN_STRING="jdbc:mysql://localhost:3306/pa?zeroDateTimeBehavior=convertToNull";
        public static final String USER="root";
        public static final String PASS="12345";
        
        public static String sec_code;
        public static String b_code;
        public static int sem;
        public static String print_name;
        
        
        public static String str;
        
    public Generate()
    {
//        //subjects need to be fetched from DB
//        /*subjects.add("COMP");
//        subjects.add("CNW");
//        subjects.add("Maths");
//        subjects.add("DBS");
//        subjects.add("SE");
//        subjects.add("OE");
//        //credits need to be fetched from DB
//        credits.put(subjects.get(0), 4);
//        credits.put(subjects.get(1), 3);
//        credits.put(subjects.get(2), 3);
//        credits.put(subjects.get(3), 3);
//        credits.put(subjects.get(4), 3);
//        credits.put(subjects.get(5), 3);
//        //labs need to be fetched from DB
//        labs.add("COMP_Lab");
//        labs.add("DBS_Lab");
//        labs.add("SE_Lab");*/
//         //Generate timetable function is called here
    }
    public Generate(String b_code1, int sem1, String sec_code1)
    {      b_code=b_code1;
           sec_code=sec_code1;
           sem=sem1;
           String temp;
           str = "";
           temp = xyz(); 
           System.out.println(temp);
           
       /*Connection conn=null;
       Statement stmt=null;
       ResultSet rs=null;
       
       try
       {   Class.forName("com.mysql.jdbc.Driver");
           conn=DriverManager.getConnection(CONN_STRING,USER,PASS);
           stmt=(Statement) conn.createStatement();
           
           
           
                      
            print_name="select s_code,s_name,s_cred from pa_branch b,pa_section s,pa_subjects sub where b.b_code=s.b_code and s.b_code=sub.b_code and s.semester = sub.sem and s.b_code='"+b_code+"' and s.sec_code='"+sec_code+"' and s.semester="+sem+";";
           
      
           rs=stmt.executeQuery(print_name);
           int c=0;
           while(rs.next())
           {
               String sub=rs.getString("s_name");
               String scred=rs.getString("s_cred");
               int scred2=Integer.parseInt(scred);
               subjects.add(sub);
               credits.put(subjects.get(c++), scred2);
               
           }
        labs.add("COMP_Lab");
        labs.add("DBS_Lab");
        labs.add("SE_Lab");
           
       }
       
       catch(Exception e)
       {
           System.out.println("Error faced! "+e);
       }
   
    
    this.generate_timetable();*/
    }
    
            
    public void generate_timetable()
    {        
        Random rand = new Random(); //random class initialised
        ArrayList<String> p_subjects = new ArrayList(); 
        int c;
        for(int j=0;j<6;j++) //for days of week
        {
            ArrayList<String> temp = new ArrayList(); //temporary array
            
            for(int q=0;q<this.subjects.size();q++)
                temp.add(this.subjects.get(q));
            int isFull = rand.nextInt(2);            
            //if no more full days are available
            if(this.fdays == 0)
                isFull = 0;
            //if fullday is randomly assigned
            if(isFull == 1)
            {
                this.fdays--;                
                for(int i=0;i<4;i++)
                {
                    int n_subjects = temp.size();
                    int random = rand.nextInt(n_subjects);
                    //if no credits available
                    if(this.credits.get(temp.get(random)) == 0)
                    {
                        p_subjects.add("NA-" + temp.get(random));
                        continue;
                    }
                    else
                    {
                       c = this.credits.get(temp.get(random));
                       c--; //Credits decremented
                       this.credits.put(temp.get(random),c);
                    }
                    
                    p_subjects.add(temp.get(random)); //subject pushed to array
                    temp.remove(random);                                                                                    
                }
                int n_labs = this.labs.size();
                int lab_index = rand.nextInt(n_labs);
                String p_lab = this.labs.get(lab_index); //lab pushed to array
                this.labs.remove(lab_index);
                p_subjects.add(p_lab);
                p_subjects.add("skip"); //just to mark end of day
            }
            else
            {                
                for(int i=0;i<4;i++)
                {
                    int n_subjects = temp.size();
                    int random = rand.nextInt(n_subjects);
                    if(this.credits.get(temp.get(random)) == 0)
                    {
                        p_subjects.add("NA-" + temp.get(random));
                        continue;
                    }
                    else
                    {
                       c = this.credits.get(temp.get(random));
                       c--;
                       this.credits.put(temp.get(random),c);
                    }
                    p_subjects.add(temp.get(random));
                    temp.remove(random);           
                    
                }
                p_subjects.add("skip");
                
            }
                    
        }        
        int cr;
        ArrayList<String> day = new ArrayList();
        int slot = 1;
        for(int i=0;i<p_subjects.size();i++)
        {                        
            int np = 0;
            while(!p_subjects.get(np).equals("skip"))
            {                        
                np++;                        
            }
            
                
            if(p_subjects.get(i).equals("skip"))
            {                
                str=str+"\n";                
                p_subjects.remove(i);
                i--;               
                day.clear();
                slot = 1;
                continue;
            }                    
            
            if(this.labs.size() != 0 && np <= 4)
            {
                p_subjects.add(i, this.labs.get(0));
                this.labs.remove(0);                  
                np++;
            }
            
            day.add(p_subjects.get(i));                                    
            int check = 0;
            if(p_subjects.get(i).contains("NA"))
            {
                for(Map.Entry<String,Integer> entry : this.credits.entrySet())
                {                    
                    if(entry.getValue() != 0)
                    {                  
                        for(int j=0;j<day.size();j++)
                        {
                            if(entry.getKey().equals(day.get(j)))
                            {                                
                                check = 1;
                                break;
                            }
                                
                        }
                        if(check == 0)
                        {
                            p_subjects.remove(i);
                            p_subjects.add(i, entry.getKey());
                            cr = entry.getValue();
                            cr--;
                            this.credits.put(entry.getKey(),cr);
                            break;
                        }
                    }
                    
                }
             
            }
            
            if(p_subjects.get(i).contains("NA"))
            {
                p_subjects.remove(i);
                p_subjects.add(i, "NA");
                
            }
            //this should be added to the 'timetable' table            
            str = str+" "+p_subjects.get(i) + "\t";
            slot++; //to insert in slot table
        }        
    }
     public static void main(String args[])
     {
         
     }
    public static String xyz()
    {
        Connection conn=null;
       Statement stmt=null;
       ResultSet rs=null;
       
       try
       {   Class.forName("com.mysql.jdbc.Driver");
           conn=DriverManager.getConnection(CONN_STRING,USER,PASS);
           stmt=(Statement) conn.createStatement();
           
           
           
           
           
            print_name="select s_code,s_name,s_cred from pa_branch b,pa_section s,pa_subjects sub where b.b_code=s.b_code and s.b_code=sub.b_code and s.semester = sub.sem and s.b_code='"+b_code+"' and s.sec_code='"+sec_code+"' and s.semester="+sem+";";
           
      
           rs=stmt.executeQuery(print_name);
           int c=0;
           while(rs.next())
           {
               String sub=rs.getString("s_name");
               String scred=rs.getString("s_cred");
               int scred2=Integer.parseInt(scred);
               subjects.add(sub);
               credits.put(subjects.get(c++), scred2);
               
           }
        labs.add("COMP_Lab");
        labs.add("DBS_Lab");
        labs.add("SE_Lab");
           
       }
       
       catch(Exception e)
       {
           System.out.println("Error faced! "+e);
       }
   
    Generate g= new Generate();
    g.generate_timetable();
        return str;
        
    }
}
