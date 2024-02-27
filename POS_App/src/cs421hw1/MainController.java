package cs421hw1;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class MainController {

    @FXML
    private Button btnSubmit;

    @FXML
    private ChoiceBox<String> cmbPackage;

    @FXML
    private Label lblRaptor1;

    @FXML
    private Label lblRaptor2;

    @FXML
    private Label lblDiscount;

    @FXML
    private ListView<String> lstItems;

    @FXML
    private TextField txtAdults;

    @FXML
    private TextField txtAdultsRaptor;

    @FXML
    private TextField txtChildRaptor;

    @FXML
    private TextField txtChildren;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtCreditCard;

    @FXML
    private TextField txtDiscount;

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private Label lblFirst;
    @FXML
    private Label lblMiddle;
    @FXML
    private Label lblLast;
    @FXML
    private Label lblZip;

    @FXML
    private TextField txtMMYY;

    @FXML
    private TextField txtMiddle;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtState;

    @FXML
    private TextField txtStreet;

    @FXML
    private TextField txtZip;

    @FXML
    private Label lblAdult;

    @FXML
    private Label lblCity;
    @FXML
    private Label lblPhone;
    @FXML
    private Label lblCC;
    @FXML
    private Label lblMMYY;
    @FXML
    private Label lblChild;
    @FXML
    private Label lblError;
    @FXML
    private Label lblState;
    // Set prices for the packages
    final double TT_ADULT = 219.75;
    final double TT_CHILD = 195.50;
    final double SP_ADULT = 155.00;
    final double SP_CHILD = 120.25;
    final double PD_ADULT = 53.00;
    final double PD_CHILD = 26.50;
    final double RAPTOR_CHILD = 55;
    final double RAPTOR_ADULT = 80;
    final double TAX = .06;
    double total = 0;
    double discount;
    // booleans to check if all the info entered is correct
    boolean vAdult = true, vChild = true, vPackage = false, vRAdult = true, vRChild = true, vDiscount= true;
    boolean firstN, middleI, lastN, city, state, zip, phone, cc, mm;
    String fullOrder;

    String selectedPackage;

    //[0] = Adult Ticket
    //[1] = Adult Raptor Ticket
    //[2] = Child Ticket
    //[3] = Child Raptor Ticket
    Ticket[] order = new Ticket[4];


    public void initialize(){
        // create an empty array for the order. Will add to receipt if not empty later
        order[0] = new Ticket("",0,0);
        order[1] = new Ticket("",0,0);
        order[2] = new Ticket("",0,0);
        order[3] = new Ticket("",0,0);
        // add the package types to an observable list, so they can be displayed in the combo box
        ObservableList<String> packages = FXCollections.observableArrayList();
        packages.add("Tyrannosaurs Terror");
        packages.add("Stegosaurus Plates");
        packages.add("Pterodactyl Droppings");
        cmbPackage.setItems(packages);
        setListeners();
    }
    // function to make sure all the text fields are filled out correctly
    public boolean checkTextFields(){
        if(txtFirstName.getText().isEmpty() || !firstN)
            return false;
        if(txtMiddle.getText().isEmpty() || !middleI)
            return false;
        if(txtLastName.getText().isEmpty() || !lastN)
            return false;
        if(txtStreet.getText().isEmpty())
            return false;
        if(txtCity.getText().isEmpty() || !city)
            return false;
        if(txtState.getText().isEmpty() || !state)
            return false;
        if(txtZip.getText().isEmpty() || !zip)
            return false;
        if(txtPhone.getText().isEmpty() || !phone)
            return false;
        if(txtCreditCard.getText().isEmpty()|| !cc)
            return false;
        if(txtMMYY.getText().isEmpty() || !mm)
            return false;
        if(total == 0)
            return false;
        // variables in the listeners to ensure the number of tickets entered is valid
        if(!vAdult || !vChild || !vPackage || !vRAdult || !vRChild || !vDiscount)
            return false;

        return true;

    }

    // set the listeners for the fields on the GUI
    // I realized after that I could have made a method for the Adult/Child and Raptor Adult/Raptor Child listeners, but
    // I think it increases the opportunity for errors if you combine them in this case
    // Every textfield and label would have to be passed into the function in the correct order everytime
    public void setListeners(){
        cmbPackage.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            // can't enter tickets until a package is selected
            lblAdult.setVisible(true);
            lblChild.setVisible(true);
            lblDiscount.setVisible(true);
            txtAdults.setVisible(true);
            txtChildren.setVisible(true);
            txtDiscount.setVisible(true);
            // verify package is selected
            vPackage = true;
            selectedPackage = newValue;
            updateSubTotal();
        });

        // listener for the adult tickets text box
        txtAdults.textProperty().addListener((observable, oldValue, newValue) -> {
                    try{
                        // make it so you can't purchase raptor tickets until adult tickets is valid
                        txtAdultsRaptor.setVisible(false);
                        lblRaptor1.setVisible(false);
                        lblAdult.setText("# of Adults Tickets");
                        // if the text box is 0 or empty, it is still valid
                        if(txtAdults.getText().equals("0") || txtAdults.getText().isEmpty()) {
                            order[0].quantity = 0;
                            order[1].quantity = 0;
                            txtAdultsRaptor.clear();
                        }
                        // verify that the number entered is not negative and an integer
                        else if(!txtAdults.getText().isEmpty() && Integer.parseInt(txtAdults.getText()) > 0) {
                            // set the number of tickets in the order array
                            order[0].quantity = Integer.parseInt(txtAdults.getText());
                            // enable the raptor text field
                            lblRaptor1.setVisible(true);
                            txtAdultsRaptor.setVisible(true);
                            lblRaptor1.setText("# of Adults Raptor Tickets");
                        }
                        // alert user they entered a negative number and set number of tickets to 0
                        else if (Integer.parseInt(txtAdults.getText()) < 0) {
                            order[0].quantity = 0;
                            lblAdult.setText("Enter a valid Integer");
                            vRAdult = false;
                            updateSubTotal();
                            // return so vAdult isn't changed to true
                            return;
                        }
                        // adult text box is correctly filled out
                        vAdult = true;
                    }
                    // if any character that isn't a number is entered
                    catch (Exception e){
                        // disable the raptor text box and change the number of tickets to 0
                        vAdult = false;
                        order[0].quantity = 0;
                        lblAdult.setText("Enter a valid Integer");
                        lblRaptor1.setVisible(false);
                        txtAdultsRaptor.setVisible(false);
                        // clear the raptor text box if the number of adult tickets isn't valid
                        txtAdultsRaptor.clear();
                        // reset adult tickets if invalid input
                        order[0].quantity = 0;
                        // reset raptor tickets if invalid input in number of adult tickets
                        order[1].quantity = 0;

                    }
            updateSubTotal();
                }
        );
        // listener for child tickets text box
        txtChildren.textProperty().addListener((observable, oldValue, newValue) -> {
                    try{
                        // make it so you can't purchase raptor tickets until child tickets is valid
                        txtChildRaptor.setVisible(false);
                        lblRaptor2.setVisible(false);
                        lblChild.setText("# of Child Tickets");
                        // if the text box is 0 or empty, it is still valid
                        if(txtChildren.getText().equals("0") || txtChildren.getText().isEmpty()) {
                            order[2].quantity = 0;
                            order[3].quantity = 0;
                            txtChildRaptor.clear();
                        }
                        // verify that the number entered is not negative and an integer
                        if(!txtChildren.getText().isEmpty() && Integer.parseInt(txtChildren.getText()) > 0) {
                            // set the number of tickets in the order array
                            order[2].quantity = Integer.parseInt(txtChildren.getText());
                            // enable the raptor tickets
                            lblRaptor2.setVisible(true);
                            txtChildRaptor.setVisible(true);

                        }
                        // alert user they entered a negative number and set number of tickets to 0
                        else if (Integer.parseInt(txtChildren.getText()) < 0) {
                            vChild = false;
                            order[2].quantity = 0;
                            lblChild.setText("Enter a valid Integer");
                            updateSubTotal();
                            // return so vChild doesn't become true
                            return;
                        }
                        vChild = true;
                    }
                    // if any character that isn't a number is entered
                    catch (Exception e){
                        // reset the quantity to 0
                        order[2].quantity = 0;
                        vChild = false;
                        if(!txtChildren.getText().equals("0")) {
                            lblChild.setText("Enter a valid Integer");
                            // disable the raptor text fields
                            lblRaptor2.setVisible(false);
                            txtChildRaptor.setVisible(false);
                            txtChildRaptor.clear();
                            // reset adult tickets if invalid input
                            order[2].quantity = 0;
                            // reset raptor tickets if invalid input in number of child tickets
                            order[3].quantity = 0;
                        }
                    }
            updateSubTotal();
                }
        );
        //listener for the Adult raptors teickets
        txtAdultsRaptor.textProperty().addListener((observable, oldValue, newValue) -> {
                    try{
                        // if the text box is 0 or empty, it is still valid
                        if(txtAdultsRaptor.getText().equals("0") || txtAdultsRaptor.getText().isEmpty()) {
                            order[1].quantity = 0;
                        }
                        // verify that the number entered is not negative and an integer
                        else if(!txtAdultsRaptor.getText().isEmpty()&& Integer.parseInt(txtAdultsRaptor.getText()) > 0) {
                            lblRaptor1.setText("# of Adults Raptor Tickets");
                            // set the number of tickets
                            order[1].quantity = Integer.parseInt(txtAdultsRaptor.getText());
                            order[1].name =  "Adult Raptor Ticket";
                            order[1].price = RAPTOR_ADULT;

                            // make sure that the number of raptor tickets is not more than the number of adult tickets
                            // you must purchase an adult ticket in order to get a raptor ticket
                            if(order[1].quantity> order[0].quantity) {
                                lblRaptor1.setText("Cannot have more Raptor tickets than Adult tickets");
                                // if there are more raptor tickets than adult tickets, reset the text box
                                order[1].quantity = 0;
                                Platform.runLater(()->{
                                    txtAdultsRaptor.clear();
                                });
                            }
                        }
                        // if you enter a negative number reset the text box and ticket total
                        else if (Integer.parseInt(txtAdultsRaptor.getText()) < 0) {
                            vRAdult = false;
                            order[1].quantity = 0;
                            lblRaptor1.setText("Enter a valid integer");
                            updateSubTotal();
                            // return so vRAdult doesn't change to true
                            return;
                        }

                        // valid input
                        vRAdult = true;
                    }
                    // any invalid input is caught here
                    catch (Exception e){
                        vRAdult = false;
                        lblRaptor1.setText("Enter a valid Integer");
                        order[1].quantity = 0;
                    }
            updateSubTotal();
                }
        );
        // listener for the Child raptor text box
        txtChildRaptor.textProperty().addListener((observable, oldValue, newValue) -> {
                    try{
                        // if the text box is 0 or empty, it is still valid
                        if(txtChildRaptor.getText().equals("0") || txtChildRaptor.getText().isEmpty()) {
                            order[3].quantity = 0;
                        }
                        // verify that the number entered is not negative and an integer
                        else if(!txtChildRaptor.getText().isEmpty() && Integer.parseInt(txtChildRaptor.getText()) > 0) {
                            lblRaptor2.setText("# of Child Raptor Tickets");
                            order[3].quantity = Integer.parseInt(txtChildRaptor.getText());
                            order[3].name =  "Child Raptor Ticket";
                            order[3].price = RAPTOR_CHILD;

                            // make sure that the number of raptor tickets is not more than the number of child tickets
                            // you must purchase a child ticket in order to get a raptor ticket
                            if(order[3].quantity> order[2].quantity) {
                                lblRaptor2.setText("Cannot have more Raptor tickets than Child tickets");
                                order[3].quantity = 0;
                                // reset the child raptor text box
                                Platform.runLater(()->{
                                    txtChildRaptor.clear();
                                });
                            }
                            // if you enter a negative number reset the text box and ticket total
                        } else if (Integer.parseInt(txtChildRaptor.getText()) < 0) {
                            vRChild = false;
                            // set number of tickets in order to 0
                            order[3].quantity = 0;
                            lblRaptor2.setText("Enter a valid integer");
                            updateSubTotal();
                            // return so vRChild isn't true
                            return;
                        }
                        // valid input
                        vRChild = true;
                        // any invalid input is caught here
                    }catch (Exception e){
                        vRChild = false;
                        lblRaptor2.setText("Enter a valid Integer");
                        order[3].quantity = 0;

                    }
            updateSubTotal();
                }
        );
        // listener for the discount text box
        txtDiscount.textProperty().addListener((observable, oldValue, newValue) -> {
                    try{
                        // get what is put into the textbox
                        String rawPercent = txtDiscount.getText();
                        lblDiscount.setText("Discount Percent");
                        // if its empty, it is valid. Its equal to 0
                        if(txtDiscount.getText().isEmpty()) {
                            discount = 0;
                            updateSubTotal();
                            return;
                        }
                        // check if the last character entered was a percent
                        if(String.valueOf(rawPercent.charAt(rawPercent.length()-1)).equals("%")){
                            rawPercent = rawPercent.substring(0,rawPercent.length()-1);
                        }
                        // make sure the discount is between 0% and 100%
                        if(!txtDiscount.getText().isEmpty() && Double.parseDouble(rawPercent) > 0 &&  Double.parseDouble(rawPercent) <= 100)  {
                            discount = Double.parseDouble(rawPercent);
                        }
                        // entered a valid integer but its not between 0 and 100
                        else{
                            lblDiscount.setText("Enter a valid Percent");
                            discount = 0;
                            return;
                        }
                        // input is valid
                        vDiscount = true;
                    } //catch any invalid input
                    catch (Exception e){
                        vDiscount = false;
                        discount = 0;
                        lblDiscount.setText("Enter a valid Percent");
                    }
                    updateSubTotal();
                }
        );
        // listener for first name text box
        txtFirstName.textProperty().addListener((observable, oldValue, newValue) -> {
            // check if the input  is all letters
                    firstN = newValue.matches("^[a-zA-Z]*$");
                    if(!firstN)
                        lblFirst.setText("Invalid Name!");
                    else
                        lblFirst.setText("First Name");
                }
        );
        // listener for middle initial
        txtMiddle.textProperty().addListener((observable, oldValue, newValue) -> {
                // limit the entry to one character
                if(newValue.length() >1) {
                    txtMiddle.setText(oldValue);
                    return;
                }
                // check if the middle initial is a letter
                middleI = newValue.matches("^[a-zA-Z]");
                if(!middleI)
                    lblMiddle.setText("Invalid Initial!");
                else
                    lblMiddle.setText("Middle Initial");
            }
        );
        // listener for lastname
        txtLastName.textProperty().addListener((observable, oldValue, newValue) -> {
                    // check if the input  is all letters
                    lastN = newValue.matches("^[a-zA-Z]*$");
                    if(!lastN)
                        lblLast.setText("Invalid Name!");
                    else
                        lblLast.setText("Last Name");
                }
        );
        // listener for city
        txtCity.textProperty().addListener((observable, oldValue, newValue) -> {
            // check if the input  is all letters
                    city = newValue.matches("^[a-zA-Z ]*$");
                    if(!city)
                        lblCity.setText("Invalid City!");
                    else
                        lblCity.setText("City");
                }
        );
        // listener for state
        txtState.textProperty().addListener((observable, oldValue, newValue) -> {
                    // limit the entry to two characters
                    if(newValue.length() >2) {
                        txtState.setText(oldValue);
                        return;
                    }
                    // check if the input is all letters and two characters long
                    state = newValue.matches("^[a-zA-Z]*$") && txtState.getText().length()==2;
                    if(!state)
                        lblState.setText("Invalid State!");
                    else
                        lblState.setText("State");
                }
        );
        txtZip.textProperty().addListener((observable, oldValue, newValue) -> {
                    // limit the entry to five characters
                    if(newValue.length() >5) {
                        txtZip.setText(oldValue);
                        return;
                    }
                    // check if the input is all letters and five characters long
                    zip = newValue.matches("^[0-9]*$") && txtZip.getText().length()==5;
                    if(!zip)
                        lblZip.setText("Invalid Zip!");
                    else
                        lblZip.setText("Zip");
                }
        );
        txtPhone.textProperty().addListener((observable, oldValue, newValue) -> {
                    // limit the entry to 12 characters
                    if(newValue.length() >12) {
                        txtPhone.setText(oldValue);
                        return;
                    }

                    // check if the input is all letters and 12 characters long and if there is a "-" in the right spot
                    phone = newValue.matches("^[0-9-]*$") && txtPhone.getText().length()==12 && checkChar(newValue.charAt(3),"-"
                    )&& checkChar(newValue.charAt(7), "-");
                    if(!phone)
                        lblPhone.setText("Invalid Phone #!");
                    else
                        lblPhone.setText("Phone Number");
                }
        );

        txtCreditCard.textProperty().addListener((observable, oldValue, newValue) -> {
                    // limit the entry to 19 characters
                    if(newValue.length() >19) {
                        txtCreditCard.setText(oldValue);
                        return;
                    }

                    // check if the input is all letters and 19 characters long and that the spaces are in the right spot
                    cc = newValue.matches("^[0-9 ]*$") && txtCreditCard.getText().length()==19 && checkChar(newValue.charAt(4), " ") && checkChar(newValue.charAt(9), " ") && checkChar(newValue.charAt(14), " ");
                    if(!cc)
                        lblCC.setText("Invalid Credit Card!");
                    else
                        lblCC.setText("Credit Card #");
                }
        );
        txtMMYY.textProperty().addListener((observable, oldValue, newValue) -> {
                    // limit the entry to 5 characters
                    if(newValue.length() >5) {
                        txtMMYY.setText(oldValue);
                        return;
                    }

                    // check if the input is all letters and 5 characters long and that the "/" is in the right spot
                    mm = txtMMYY.getText().matches("^\\d{2}/\\d{2}$") && txtMMYY.getText().length()==5 && checkChar(newValue.charAt(2), "/");
                    if(!mm)
                        lblMMYY.setText("Invalid MM/YY!");
                    else
                        lblMMYY.setText("MM/YY");
                }
        );

    }
    // method to check if the character at specific index matches
    // used for validating phone number and credit card
    public boolean checkChar(char c, String s){
        return String.valueOf(c).equals(s);

    }
    // method that updates the receipt in real time
    public void updateSubTotal(){
        fullOrder = "";
        // clear out the items in the list
        lstItems.getItems().clear();
        total = 0;
        // check which package is selected to get the correct prices
        if(selectedPackage.equals("Tyrannosaurs Terror")){
            order[0].price = TT_ADULT;
            order[2].price = TT_CHILD;
        }
        else if(selectedPackage.equals("Stegosaurus Plates")){
            order[0].price = SP_ADULT;
            order[2].price = SP_CHILD;
        }
        else if (selectedPackage.equals("Pterodactyl Droppings")){
            order[0].price = PD_ADULT;
            order[2].price = PD_CHILD;
        }
        else{
            return;
        }
        // get the name of the item to put on the receipt
        order[0].name = selectedPackage + " Adult Ticket";
        order[2].name = selectedPackage + " Child Ticket";

        // add items from the order to the receipt if the customer selected it
        // also add its price to the total
        for(int i=0; i< order.length;i++){
            if(order[i].quantity != 0) {
                lstItems.getItems().add(order[i].toString());
                total += order[i].price * order[i].quantity;
                fullOrder += order[i].toString() + "\n";
;            }
        }
        lstItems.getItems().add("");
        // if there is a discount, apply it
        if(discount !=0){
            lstItems.getItems().add("-$"+ String.format("%.2f",(total * (discount/100))) + "\t"+ discount + "% Discount");
            total = total- (total * (discount/100));
        }
        // add the other info to the receipt
        lstItems.getItems().add("Subtotal:\t$" + String.format("%.2f",total));
        lstItems.getItems().add("Tax:\t$" + String.format("%.2f",(TAX* total)));
        lstItems.getItems().add("Total:  $" + String.format("%.2f",(total + (TAX* total))));

    }

    @FXML
    void submitForm(ActionEvent event) {
        // if all fields are correct, allow order to be submitted
        // when submitted, create a popup that shows order confirmation and clear fields
        if(checkTextFields()){
            lblError.setVisible(false);
            Alert a = new Alert(Alert.AlertType.INFORMATION);

            a.setTitle("Order submitted");
            // set the info that will be displayed on the order confirmation
            a.setContentText(txtFirstName.getText() + " " + txtLastName.getText() +"'s order has been confirmed! \n" +
                    fullOrder  + "\n"+
                    "Discount: " + discount + "%\n" +
                    "Subtotal:\t$" + String.format("%.2f",total)+
                    "\nTax:\t$" + String.format("%.2f",(TAX* total)) +
                    "\nTotal: $" +String.format("%.2f", (total + (TAX* total))));
            a.setHeaderText("Thank you!");
            a.showAndWait();
            clearFields();

        } else if(!checkTextFields()) {
            lblError.setVisible(true);
        }
    }
    // method to clear all fields when order is submitted
    public void clearFields(){
            cmbPackage.getSelectionModel().selectFirst();
            txtAdults.setText("0");
            txtChildren.setText("0");
            txtChildRaptor.setText("0");
            txtAdultsRaptor.setText("0");
            txtDiscount.clear();
            txtFirstName.clear();
            txtMiddle.clear();
            txtLastName.clear();
            txtStreet.clear();
            txtCity.clear();
            txtState.clear();
            txtZip.clear();
            txtPhone.clear();
            txtCreditCard.clear();
            txtMMYY.clear();
            lstItems.getItems().clear();
            //reset labels because clearing activates the listeners
            lblMiddle.setText("Middle Initial");
            lblPhone.setText("Phone Number");
            lblZip.setText("Zip");
            lblCC.setText("Credit Card Number");
            lblMMYY.setText("MM/YY");
            lblState.setText("State");

    }

}
// class thats for the items in the order
// every Ticket has a price, a number of items(quantity) and a name
class Ticket{
    double price;
    int quantity;
    String name;

    public Ticket(String name, int quantity, double price) {
        this.price = price;
        this.quantity = quantity;
        this.name = name;
    }

    // to string for the receipt
    @Override
    public String toString() {
        return "$" + String.format("%.2f",(this.quantity*this.price)) + "\t" + this.quantity + " x " + this.name;
    }
}
