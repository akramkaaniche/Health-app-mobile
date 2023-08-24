/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package aura.gui.article;

import aura.entities.User;
import aura.entities.article;
import aura.gui.HomeForm;
import aura.gui.activiteTherapie.Listact;
import aura.gui.activiteTherapie.Listth;
import aura.gui.challenge_classement.Classement;
import aura.gui.challenge_classement.ListChallengesForm;
import aura.gui.challenge_classement.MesChallenges;
import aura.gui.entraide.AllQuestion;
import aura.gui.gestionUser.HomeClient;
import aura.gui.objectif.ObjectifForm;
import aura.services.ServiceArticle;
import aura.services.ServiceUser;
import com.codename1.components.MultiButton;
import com.codename1.components.SpanLabel;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import java.io.IOException;
import java.util.ArrayList;
//Imports GUI
import aura.gui.gestionUser.HomeClient;
import aura.gui.HomeForm;
import aura.gui.challenge_classement.Classement;
import aura.gui.challenge_classement.ListChallengesForm;
import aura.gui.activiteTherapie.Listact;
import aura.gui.activiteTherapie.Listact;
import aura.gui.activiteTherapie.Listth;
import aura.gui.activiteTherapie.Listth;
import aura.gui.challenge_classement.MesChallenges;
import aura.gui.article.HomeArticle;
import aura.gui.entraide.AllQuestion;
import aura.gui.objectif.ObjectifForm;
// End 
// SideBar Imports
import com.codename1.ui.Toolbar;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import java.io.IOException;

//End 

/**
 *
 * @author akram
 */

    public class listefavoris extends Form{
Form current;
     String username ;
    User user;
 ServiceArticle sa = new ServiceArticle();
    ArrayList<article> List;
    
        public void sideBar(String username)
    {
   Toolbar tb = this.getToolbar();
        //Image icon=theme.getImage("logo.png");
        Container topbar = BorderLayout.east(new Label());
        topbar.add(BorderLayout.SOUTH, new Label("AURA"));
        topbar.setUIID("SideCommand");
        tb.addComponentToSideMenu(topbar);

        tb.addMaterialCommandToSideMenu("Therapie", FontImage.MATERIAL_RECORD_VOICE_OVER, ev -> {
            new Listth(current, username);

        });
        tb.addMaterialCommandToSideMenu("Activite", FontImage.MATERIAL_EXTENSION, ev -> {
            new Listact(current, username);

        });
        tb.addMaterialCommandToSideMenu("Objectifs", FontImage.MATERIAL_CHECK, ev -> {
            new ObjectifForm(username).show();

        });
        tb.addMaterialCommandToSideMenu("Articles", FontImage.MATERIAL_ARTICLE, ev -> {
            new HomeArticle(username).show();

        });
        tb.addMaterialCommandToSideMenu("Liste des Challenges", FontImage.MATERIAL_ALL_INBOX, ev -> {
            new ListChallengesForm(username).show();
        });
        tb.addMaterialCommandToSideMenu("Mes Challenges", FontImage.MATERIAL_TOUR, ev -> {
            new MesChallenges(username).show();
        });
        tb.addMaterialCommandToSideMenu("Classement", FontImage.MATERIAL_LEADERBOARD, ev -> {
            new Classement(username).show();
        });
        tb.addMaterialCommandToSideMenu("Entraide", FontImage.MATERIAL_CONTACT_PAGE, ev -> {
            new AllQuestion(username).show();
        });
        tb.addMaterialCommandToSideMenu("Mon Profil", FontImage.MATERIAL_ACCOUNT_CIRCLE, ev -> {
            try {
                new HomeClient(username).show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });   
    
    
    }
  
    public listefavoris(String username){
        this.username=username;
       this.user=ServiceUser.getInstance().getUser(this.username);
       sideBar(username);
       this.setTitle("FAVORIS");
        this.setLayout(BoxLayout.y());
        
        
        
        
        
        
        
        
        
        //SpanLabel tasksListSP = new SpanLabel();
       // tasksListSP.setText(ServiceArticle.getInstance().getAllObjectifs().toString());
        
        //this.add(tasksListSP);
         Container cnt2 = new Container(BoxLayout.y());
        cnt2.setScrollableY(true);
        this.add(cnt2);
        
        
          List= ServiceArticle.getInstance().getAllfavo(user.getId());      
       for(int i=0; i < List.size() ; i++){
           MultiButton mb = new MultiButton(List.get(i).getTitre());
           mb.setTextLine2(List.get(i).getTheme());
           mb.setTextLine3(List.get(i).getNom_auteur());
          int id = List.get(i).getId();
          String Titre =List.get(i).getTitre();
          String Theme =List.get(i).getTheme();
          String Auteur =List.get(i).getNom_auteur();
          String Date =List.get(i).getDate();
          String Article=List.get(i).getArticle();
          String idUser=List.get(i).getId_user();
           mb.addActionListener(e -> new detailarticle(user.getId(),id, Titre, Theme, Auteur, Date, Article,true,username).show());
           cnt2.add(mb);
       }
        getToolbar().addMaterialCommandToLeftBar("", FontImage.MATERIAL_ARROW_BACK, ev-> new HomeArticle(username).showBack());
}
        
      
    }

