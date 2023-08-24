/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aura.gui.gestionUser;

import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;

import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import aura.entities.User;
import aura.services.ServiceUser;
import aura.services.Validator;
import com.codename1.capture.Capture;

import com.codename1.ui.events.ActionEvent;

import java.io.IOException;


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
import aura.utils.Statics;
// End 
// SideBar Imports
import com.codename1.ui.Toolbar;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
//End 


/**
 *
 * @author SeifBS
 */
public class HomeClient extends Form {

    Form current;
    public static String username;
    public User user;
    public String picture;
    public int musicIndice = 1;

    public HomeClient() {

    }
    
    
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
                new HomeClient(username);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });   
    
    }

    public HomeClient(String username) throws IOException {
        

        this.username = username;
        sideBar(username);
        this.user = ServiceUser.getInstance().getUser(this.username);
        

        Button sms;

        if (this.user.getSms().equals("N")) {
            sms = new Button("Activer l'identification à deux facteurs");
        } else {
            sms = new Button("Desactiver l'identification à deux facteurs");

        }
        add(sms);

        sms.addActionListener((e) -> {
            Boolean result = ServiceUser.getInstance().smsAuthentification(this.user);
            try {
                new HomeClient(this.username);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            Dialog.show("Modification", "Modification Effectuée", new Command("OK"));

        });

        Dialog d = new Dialog("Bienvenue");
        TextArea popupBody = new TextArea("Salut " + this.user.getNom().toUpperCase() + " " + this.user.getPrenom().toUpperCase() + " L'equipe Aura est heureuse de te souhaiter la bienvenue.", 3, 20);
        popupBody.setUIID("PopupBody");
        popupBody.setEditable(false);
        d.setLayout(new BorderLayout());
        d.add(BorderLayout.CENTER, popupBody);
        current = this; //Récupération de l'interface(Form) en cours
        setTitle("Modifer vos parametres");
        Validator v = new Validator();

        Image image = Image.createImage(Statics.directory+this.user.getPicture());
        int w =image.getWidth();
        int h = image.getHeight();      
        Image maskImage = Image.createImage(w, h);
        Graphics g = maskImage.getGraphics();
        g.setAntiAliased(true);
        g.setColor(0x000000);
        g.fillRect(0, 0, w, h);
        g.setColor(0xffffff);
        g.fillArc(0, 0, w, h, 0, 360);
        Object mask = maskImage.createMask();        
        Image maskedImage = image.applyMask(mask);
        Label picture = new Label(maskedImage);
        


        TextField nom = new TextField(this.user.getNom(), "Nom");
        TextField prenom = new TextField(this.user.getPrenom(), "Prenom");
        TextField tel = new TextField(this.user.getTel(), "Numero");
        Button btnModifier = new Button("Confirmer");
        d.showPopupDialog(btnModifier);
        Button btnModifierPassword = new Button("Modifier mon mot de passe");
        Button uploadPicture = new Button("Modifier Votre photo");

        uploadPicture.addActionListener((ActionEvent e) -> {
            try {
                String imgPath = Capture.capturePhoto();
                String link = imgPath.toString();
                int pod = link.indexOf("/", 4);
                String news = link.substring(pod + 37, link.length());

                this.user.setPicture(news);

//              
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        });

        btnModifier.addActionListener((e) -> {

            if ((nom.getText().length() == 0) || (prenom.getText().length() == 0)) {
                Dialog.show("Attention", "Veuillez verifier vos données ", new Command("OK"));
            } else if (!v.test_Tel(tel.getText())) {
                Dialog.show("Attention", "Veuillez verifier votre numero telephonique", new Command("OK"));
            } else {

                //User u = new User(Integer.parseInt(tfStatus.getText()), tfName.getText());
                User u = new User(this.user.getId(), nom.getText(), prenom.getText(), tel.getText(),this.user.getPicture());

                if (ServiceUser.getInstance().modifierUser(u)) {
                    Dialog.show("Success", "Modification effectuée", new Command("OK"));
                } else {
                    Dialog.show("ERROR", "Server error", new Command("OK"));
                }
            }
        });

        btnModifierPassword.addActionListener((e) -> {

            ModifierPassword modiferPassword = new ModifierPassword(current, this.username);
            // homeClient(current).show();

            modiferPassword.show();
        });

        Button log_out = new Button("Se Deconnecter");
        FontImage.setMaterialIcon(log_out, FontImage.MATERIAL_LOGOUT);
        log_out.addActionListener((e) -> {

            try {
                new HomeForm().show();
                // homeClient(current).show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });

        Button pause = new Button("Pause");
        pause.addActionListener((e) -> {

            HomeForm.music.pause();
        });
        Button play = new Button("Play");
        play.addActionListener((e) -> {

            HomeForm.music.play();
        });
        Button changeMusic = new Button("Changer la musique");
         changeMusic.addActionListener((e) -> {

            HomeForm.music.cleanup();
            
            
        });

        //addAll(picture,nom,prenom,tel,btnModifier,btnModifierPassword,log_out);
        addAll(picture,nom, prenom, tel, btnModifier, btnModifierPassword, uploadPicture, log_out,play,pause,changeMusic);


    }

}
