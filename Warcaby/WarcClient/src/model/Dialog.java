package model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollBar;
public class Dialog {
	static String about="Gra jest rozgrywana na planszy 8x8 pól pokolorowanych na przemian na kolor jasny i ciemny.\r\n" + 
			"\r\n" + 
			"Ka¿dy gracz rozpoczyna grê z dwunastoma pionami (jeden koloru bia³ego, drugi -- czerwonego) ustawionymi na ciemniejszych polach planszy.\r\n" + 
			"\r\n" + 
			"Jako pierwszy ruch wykonuje graj¹cy pionami bia³ymi, po czym gracze wykonuj¹ na zmianê kolejne ruchy.\r\n" + 
			"\r\n" + 
			"Celem gry jest zbicie wszystkich pionów przeciwnika (w tym damek) albo zablokowanie wszystkich, które pozostaj¹ na planszy. Jeœli ¿aden z graczy nie jest w stanie tego osi¹gn¹æ (ka¿dy z graczy wykona po 15 ruchów damkami bez zmniejszania liczby pionów pozostaj¹cych na planszy), nastêpuje remis.\r\n" + 
			"\r\n" + 
			"Piony mog¹ poruszaæ siê o jedno pole do przodu po przek¹tnej na wolne pola.\r\n" + 
			"\r\n" + 
			"Bicie pionem nastêpuje przez przeskoczenie s¹siedniego pionu (lub damki) przeciwnika na pole znajduj¹ce siê tu¿ za nim po przek¹tnej (pole to musi byæ wolne).\r\n" + 
			"\r\n" + 
			"Piony mog¹ biæ zarówno do przodu, jak i do ty³u.\r\n" + 
			"\r\n" + 
			"W jednym ruchu wolno wykonaæ wiêcej ni¿ jedno bicie tym samym pionem, przeskakuj¹c przez kolejne piony (damki) przeciwnika.\r\n" + 
			"\r\n" + 
			"Bicia s¹ obowi¹zkowe.Kiedy istnieje kilka mo¿liwych biæ, mo¿na wykonaæ dowolne (niekoniecznie maksymalne)."
			+ "Podczas bicia nie mo¿na przeskakiwaæ wiêcej ni¿ raz przez ten sam pion (damkê)\r\n" + 
			"\r\n" + 
			"Pion, który dojdzie do ostatniego rzêdu planszy, staje siê damk¹, jeœli znajdzie siê tam w wyniku bicia, mo¿e w tym samym ruchu kontynuowaæ bicie.\r\n" + 
			"\r\n" + 
			"Damki mog¹ poruszaæ siê w jednym ruchu o dowoln¹ liczbê pól do przodu lub do ty³u po przek¹tnej, zatrzymuj¹c siê na wolnych polach.\r\n" + 
			"\r\n" + 
			"Bicie damk¹ jest mo¿liwe z dowolnej odleg³oœci po linii przek¹tnej i nastêpuje przez przeskoczenie pionu (lub damki) przeciwnika, "
			+ "za którym musi znajdowaæ siê co najmniej jedno wolne pole -- damka przeskakuje na dowolne z tych pól i mo¿e kontynuowaæ bicie (na tej samej lub prostopad³ej linii).\r\n";
	public static void infoDialog() {
		Alert errorAlert= new Alert(Alert.AlertType.INFORMATION);
		errorAlert.setTitle("ZASADY GRY");
		errorAlert.setHeaderText("Warcaby rosyjskie");
		errorAlert.setContentText(about);
		errorAlert.setX(100);
		errorAlert.setY(50);
		errorAlert.getDialogPane().setMinSize(800, 400);
		errorAlert.getDialogPane().setMaxSize(800, 400);
		
		//errorAlert.setResizable(true);
		errorAlert.showAndWait();
	}
}
