/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



package myBean;
import javax.ejb.*;
import javax.jms.*;
import javax.naming.*;
import java.util.*;
import java.io.*;
import java.util.logging.*;
import java.util.logging.FileHandler;




public class MessageLogger implements MessageDrivenBean
{
 private MessageDrivenContext messageContext;

 public void ejbCreate() 
 {
  }

 public void ejbRemove()
 {
  /* no implementation is necessary for this MDB*/
 }

 public void onMessage(Message message)
 {

     Logger log = Logger.getLogger(ReadMessageMDB.class.getName());

  try
  {
   /* retrieve the initial context for the lookup */
   Context ic =  new InitialContext();

   /*invoke the LogMessage entity bean to process the message*/
   /* retrieve the home interface of the LogMessage bean*/
   /* making sure to narrow the returned object to LogMessageHome*/
   ReadMessageMDB home = (ReadMessageMDB)

      javax.rmi.PortableRemoteObject.narrow(

	      ic.lookup("java:comp/env/logMessages"),

		      ReadMessageMDB.class);

   home.onMessage(message);
   FileHandler fileHandler = new FileHandler("data.log", true);        
   log.addHandler(fileHandler);
   

  }
   catch(Exception e)
  {
   throw new EJBException(e);
  }
 }
 
 public void setMessageDrivenContext(MessageDrivenContext context)
 {
  /* As with all EJBs, you must set the context in order to be 
     able to use it at another time within the MDB methods. */
  this.messageContext = context; 
 }
}