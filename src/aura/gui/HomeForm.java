/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aura.gui;

import aura.gui.gestionUser.forgetPasswordCheck;
import aura.gui.gestionUser.AddClientForm;
import aura.gui.gestionUser.facebookLogin;
import aura.gui.gestionUser.VerificationCodeSms;
import aura.entities.User;
import aura.gui.objectif.ObjectifForm;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.ui.Button;
import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.FontImage;
import com.codename1.ui.Form;
import com.codename1.ui.Image;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.ImageIO;
import aura.services.ServiceUser;
import com.codename1.media.Media;
import com.codename1.media.MediaManager;
import com.codename1.ui.Container;
import java.io.IOException;
import java.io.OutputStream;
import static java.lang.String.valueOf;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author bhk
 */
public class HomeForm extends Form {

    Form current;
    public static String username = "";
    public static String verificationCode;
    public static Media music = null;

    public void musicPlay() {
        ArrayList<String> musiqueArray = new ArrayList<String>();
        musiqueArray.add("music0.mp3");
        musiqueArray.add("music1.mp3");
        musiqueArray.add("music2.mp3");

        String directory = "file://C:/Users/SeifBS/Downloads/";

        int random = new Random().nextInt(3);
        System.out.println("random:" + random);

        try {
            this.music = MediaManager.createMedia(directory + musiqueArray.get(random), false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.music.play();

    }

    public HomeForm() throws IOException {
        musicPlay();

        current = this; //Récupération de l'interface(Form) en cours
        setTitle("Acceuil");
        setLayout(BoxLayout.y());

        Button createAccount = new Button("Creer un compte");
        FontImage.setMaterialIcon(createAccount, FontImage.MATERIAL_CREATE);
        Button seConnecter = new Button("Se connecter");
        FontImage.setMaterialIcon(seConnecter, FontImage.MATERIAL_LOGIN);
        TextField email = new TextField("", "Adresse Email");
        TextField password = new TextField("", "Mot de passe");
        Button forgetPassword = new Button("mot de passe oublié");
        Button seConnecterFacebook = new Button("Se connecter avec Facebook");
        Button capture = new Button("");
        FontImage.setMaterialIcon(capture, FontImage.MATERIAL_PHOTO_CAMERA);
        seConnecterFacebook.addActionListener((e) -> {
            facebookLogin facebookLogin;
            try {
                facebookLogin = new facebookLogin(current);
                facebookLogin.show();

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        add(seConnecterFacebook);
        FontImage.setMaterialIcon(seConnecterFacebook, FontImage.MATERIAL_FACEBOOK);

        password.setConstraint(TextField.PASSWORD);

        seConnecter.addActionListener((e) -> {

            String result = ServiceUser.getInstance().loginCheck(email.getText(), password.getText());

            if ("true".equals(result)) {
                User user = new User();
                this.username = email.getText();
                user = ServiceUser.getInstance().getUser(this.username);
                if (user.getSms().equals("Y")) {
                    Random random = new Random();
                    this.verificationCode = valueOf(random.nextInt()).substring(1, 6);
                    // Boolean sendSmsMethod = ServiceUser.getInstance().sendSms(user.getId(),this.verificationCode);
                    System.out.println(this.verificationCode);
                    Dialog.show("Verification", "Veuillez verifier votre telephone", new Command("OK"));

                    new VerificationCodeSms(current, this.username).show();
                } else {

                    //HomeClient homeClient = new HomeClient(current, this.username);
                    //homeClient.show();
                    new ObjectifForm(this.username).show();

                }
            } else {
                Dialog.show("Attention", "Veuillez verifier vos données ", new Command("OK"));

            }

        });

        //e -> new AddTaskForm(current).show()
        //  seConnecter.addActionListener(e -> new ListTasksForm(current).show());
        createAccount.addActionListener((e) -> {

            new AddClientForm(current).show();

        });
        capture.addActionListener((e) -> {
            Image screenshot = Image.createImage(this.getWidth(), this.getHeight());
            this.revalidate();
            this.setVisible(true);
            this.paintComponent(screenshot.getGraphics(), true);
            String directory = "file://C:/Users/SeifBS/Documents/";
            String imageFile = directory + "screenshot" + valueOf(new Random().nextInt()).substring(1, 6) + ".png";
            //FileSystemStorage.getInstance().getAppHomePath().
            System.out.println(imageFile);
            try (OutputStream os = FileSystemStorage.getInstance().openOutputStream(imageFile)) {
                ImageIO.getImageIO().save(screenshot, os, ImageIO.FORMAT_PNG, 1);
            } catch (IOException err) {
                Log.e(err);
            }
        });

        forgetPassword.addActionListener((e) -> {

            new forgetPasswordCheck(current).show();

        });

        Container containerLigne1 = new Container(BoxLayout.x());
        containerLigne1.add(seConnecter);
        containerLigne1.add(createAccount);

        //addAll(email, password, seConnecter, createAccount, forgetPassword, capture);
        //    add(BorderLayout.NORTH,capture);
        addAll(email, password, containerLigne1, forgetPassword, capture);

    }

    public String getPictureTitle(String path) {
        System.out.println("hey" + path);
        int i;
        String pictureTitle = "";
        for (i = path.length(); i >= 0; i--) {

            if (path.charAt(i) == "/".charAt(0)) {
                return pictureTitle;
            }
            pictureTitle += path.charAt(i);
        }
        System.out.println("hey" + path.charAt(i));
        return pictureTitle;
    }

}
