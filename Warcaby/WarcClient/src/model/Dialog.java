package model;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollBar;
public class Dialog {
	static String about="Gra jest rozgrywana na planszy 8x8 p�l pokolorowanych na przemian na kolor jasny i ciemny.\r\n" + 
			"\r\n" + 
			"Ka�dy gracz rozpoczyna gr� z dwunastoma pionami (jeden koloru bia�ego, drugi -- czerwonego) ustawionymi na ciemniejszych polach planszy.\r\n" + 
			"\r\n" + 
			"Jako pierwszy ruch wykonuje graj�cy pionami bia�ymi, po czym gracze wykonuj� na zmian� kolejne ruchy.\r\n" + 
			"\r\n" + 
			"Celem gry jest zbicie wszystkich pion�w przeciwnika (w tym damek) albo zablokowanie wszystkich, kt�re pozostaj� na planszy. Je�li �aden z graczy nie jest w stanie tego osi�gn�� (ka�dy z graczy wykona po 15 ruch�w damkami bez zmniejszania liczby pion�w pozostaj�cych na planszy), nast�puje remis.\r\n" + 
			"\r\n" + 
			"Piony mog� porusza� si� o jedno pole do przodu po przek�tnej na wolne pola.\r\n" + 
			"\r\n" + 
			"Bicie pionem nast�puje przez przeskoczenie s�siedniego pionu (lub damki) przeciwnika na pole znajduj�ce si� tu� za nim po przek�tnej (pole to musi by� wolne).\r\n" + 
			"\r\n" + 
			"Piony mog� bi� zar�wno do przodu, jak i do ty�u.\r\n" + 
			"\r\n" + 
			"W jednym ruchu wolno wykona� wi�cej ni� jedno bicie tym samym pionem, przeskakuj�c przez kolejne piony (damki) przeciwnika.\r\n" + 
			"\r\n" + 
			"Bicia s� obowi�zkowe.Kiedy istnieje kilka mo�liwych bi�, mo�na wykona� dowolne (niekoniecznie maksymalne)."
			+ "Podczas bicia nie mo�na przeskakiwa� wi�cej ni� raz przez ten sam pion (damk�)\r\n" + 
			"\r\n" + 
			"Pion, kt�ry dojdzie do ostatniego rz�du planszy, staje si� damk�, je�li znajdzie si� tam w wyniku bicia, mo�e w tym samym ruchu kontynuowa� bicie.\r\n" + 
			"\r\n" + 
			"Damki mog� porusza� si� w jednym ruchu o dowoln� liczb� p�l do przodu lub do ty�u po przek�tnej, zatrzymuj�c si� na wolnych polach.\r\n" + 
			"\r\n" + 
			"Bicie damk� jest mo�liwe z dowolnej odleg�o�ci po linii przek�tnej i nast�puje przez przeskoczenie pionu (lub damki) przeciwnika, "
			+ "za kt�rym musi znajdowa� si� co najmniej jedno wolne pole -- damka przeskakuje na dowolne z tych p�l i mo�e kontynuowa� bicie (na tej samej lub prostopad�ej linii).\r\n";
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
