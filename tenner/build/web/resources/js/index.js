/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$("#search").change( function () {
        $(".gig_list_heading").html("Search results for '"+ $("#search").val() +"' ");
      
   });
