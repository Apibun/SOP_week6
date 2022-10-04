package com.example.week6.view;

import com.example.week6.pojo.Wizard;
import com.example.week6.pojo.Wizards;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

@Route(value="/mainPage.it")
public class MainWizardView extends VerticalLayout {
    private TextField fullname, money;
    private RadioButtonGroup<String> gender;
    private ComboBox<String> position, school, house;
    private HorizontalLayout hl;
    private Button leftarrow, create, update, delete, rightarrow;
    private Wizards wizards;

    private Wizard wizard;
    private int index = -1;

    public MainWizardView(){
        this.wizards = new Wizards();
        fullname = new TextField();
        fullname.setPlaceholder("Fullname");
        gender = new RadioButtonGroup<>();
        gender.setLabel("Gender : ");
        gender.setItems("Male", "Female");
        position = new ComboBox<>();
        position.setItems("Student", "Teacher");
        position.setPlaceholder("Position");
        money = new TextField("Dollars");
        money.setPrefixComponent(new Span("$"));
        school = new ComboBox<>();
        school.setItems("Hogwarts", "Beauxbatons", "Durmstrang");
        school.setPlaceholder("School");
        house = new ComboBox<>();
        house.setItems("Gryffindor", "Ravenclaw", "Hufflepuff", "Slyther");
        house.setPlaceholder("House");
        hl = new HorizontalLayout();
        leftarrow = new Button("<<");
        create = new Button("Create");
        update = new Button("Update");
        delete = new Button("Delete");
        rightarrow = new Button(">>");
        hl.add(leftarrow, create, update, delete, rightarrow);
        this.add(fullname, gender, position, money, school, house, hl);
        this.getWizard();

        leftarrow.addClickListener(event -> {
           if (this.index >= 1){
               this.index -= 1;
               this.callWizard();
           }
           else if (this.index <= 0){
               this.index = 0;
           }
        });

        rightarrow.addClickListener(event -> {
            this.index += 1;
            if (this.index >= this.wizards.getModel().size()-1){
                this.index = this.wizards.getModel().size()-1;
            }

            this.callWizard();
        });

        create.addClickListener(event -> {
            Wizard wizard = new Wizard();
            wizard.setSex(gender.getValue().equals("Male")?"m":"f");
            wizard.setName(fullname.getValue());
            wizard.setSchool(school.getValue());
            wizard.setHouse(house.getValue());
            wizard.setMoney(Integer.parseInt(money.getValue()));
            wizard.setPosition(position.getValue());

            Wizard out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addWizard")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(wizard)
                    .retrieve()
                    .bodyToMono(Wizard.class)
                    .block();

                new Notification("Wizard has been Created", 1000).open();
                this.getWizard();
                this.index += 1;
                this.callWizard();
        });

        update.addClickListener(event -> {
            Wizard wizard = this.wizards.getModel().get(this.index);
            wizard.setSex(gender.getValue().equals("Male")?"m":"f");
            wizard.setName(fullname.getValue());
            wizard.setSchool(school.getValue());
            wizard.setHouse(house.getValue());
            wizard.setMoney(Integer.parseInt(money.getValue()));
            wizard.setPosition(position.getValue());

            Wizard out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/updateWizard")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(wizard)
                    .retrieve()
                    .bodyToMono(Wizard.class)
                    .block();

                new Notification("Wizard has been Updated", 1000).open();
                this.getWizard();
        });

        delete.addClickListener(event -> {
            Wizard wizard = this.wizards.getModel().get(this.index);
            Boolean out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/deleteWizard")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(wizard)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            if (out) {
                new Notification("Wizard has been Removed", 1000).open();
                this.index -= 1;
                this.getWizard();
                this.callWizard();
            }
        });

    }

    public void getWizard() {
        ArrayList<Wizard> w = WebClient.create()
                .get()
                .uri("http://localhost:8080/wizards")
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<ArrayList<Wizard>>() {})
                .block();
        this.wizards.setModel(w);
    }

    public void callWizard(){
        Wizard wizards = this.wizards.getModel().get(this.index);
        fullname.setValue(wizards.getName());
        if (wizards.getSex().equals("m")){
            gender.setValue("Male");
        }
        else{
            gender.setValue("Female");
        }
        position.setValue(wizards.getPosition());
        money.setValue(String.valueOf(wizards.getMoney()));
        school.setValue(wizards.getSchool());
        house.setValue(wizards.getHouse());
    }
}
