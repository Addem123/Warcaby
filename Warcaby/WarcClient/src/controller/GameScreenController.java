package controller;


import java.util.Comparator;
import java.util.HashMap;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Data;

public class GameScreenController {
	private ObservableList<Data> gamesList = FXCollections.observableArrayList();

	MainScreenController mainScreenController;

	public void setMainScreenController(MainScreenController mainScreenController) {
		this.mainScreenController = mainScreenController;
	}

	private Stage myGameStage;

	public void setMyGameStage(Stage myGameStage) {
		this.myGameStage = myGameStage;
	}

	@FXML
	private TableView<Data> resultsTable;
	@FXML
	private TableColumn<Data, Integer> points;
	@FXML
	private TableColumn<Data, String> whitePlayer;

	@FXML
	private TableColumn<Data, String> redPlayer;

	@FXML
	private TableColumn<Data, String> gameStatus;

	@FXML
	private TableColumn<Data, String> winner;

	@FXML
	private TableColumn<Data, Integer> wins;

	@FXML
	private TableColumn<Data, Integer> defeats;

	@FXML
	private TableColumn<Data, Integer> ties;

	public void initialize() throws InterruptedException {
		gamesList.clear();
		whitePlayer.setCellValueFactory(new PropertyValueFactory<Data, String>("whitePlayer"));
		redPlayer.setCellValueFactory(new PropertyValueFactory<Data, String>("redPlayer"));
		gameStatus.setCellValueFactory(new PropertyValueFactory<Data, String>("gameStatus"));
		winner.setCellValueFactory(new PropertyValueFactory<Data, String>("winner"));
		points.setCellValueFactory(new PropertyValueFactory<Data, Integer>("points"));
		wins.setCellValueFactory(new PropertyValueFactory<Data, Integer>("wins"));
		defeats.setCellValueFactory(new PropertyValueFactory<Data, Integer>("defeats"));
		ties.setCellValueFactory(new PropertyValueFactory<Data, Integer>("ties"));
	}

	@FXML
	void showRanking() {
		gamesList.clear();
		makeRanking();
		resultsTable.setItems(gamesList);
		redPlayer.setVisible(false);
		gameStatus.setVisible(false);
		winner.setVisible(false);
		sort();
	}

	@FXML
	void showMyGames() {
		gamesList.clear();
		mainScreenController.getResults().forEach(action -> {
			String whitePlayer = action.getWhitePlayer();
			String redPlayer = action.getRedPlayer();
			if (whitePlayer.equals(mainScreenController.getMyNick().getText())
					|| redPlayer.equals(mainScreenController.getMyNick().getText())) {
				gamesList.add(action);
			}
		});
		resultsTable.setItems(gamesList);
		redPlayer.setVisible(true);
		gameStatus.setVisible(true);
		winner.setVisible(true);
	}

	public void makeRanking() {

		HashMap<String, Data> map = new HashMap<String, Data>();
		mainScreenController.getResults().forEach(action -> {
			String gameStatus = action.getGameStatus();
			String whitePlayer = action.getWhitePlayer();
			String redPlayer = action.getRedPlayer();
			String winner = action.getWinner();
			if (gameStatus.equals("Ended")) {
				if (whitePlayer.equals(winner)) {
					if (map.containsKey(whitePlayer))
						map.replace(whitePlayer,
								new Data(whitePlayer, (map.get(whitePlayer).getPoints() + 3),
										(map.get(whitePlayer).getWins() + 1), (map.get(whitePlayer).getDefeats()),
										(map.get(whitePlayer).getTies())));
					else
						map.put(whitePlayer, new Data(whitePlayer, 3, 1, 0, 0));

					if (!map.containsKey(redPlayer))
						map.put(redPlayer, new Data(redPlayer, 0, 0, 1, 0));
					else
						map.replace(redPlayer,
								new Data(redPlayer, (map.get(redPlayer).getPoints()),
										(map.get(redPlayer).getWins()), (map.get(redPlayer).getDefeats()+1),
										(map.get(redPlayer).getTies())));
				} else if (redPlayer.equals(winner)) {
					if (map.containsKey(redPlayer))
						map.replace(redPlayer,
								new Data(redPlayer, (map.get(redPlayer).getPoints() + 3),
										(map.get(redPlayer).getWins() + 1), (map.get(redPlayer).getDefeats()),
										(map.get(redPlayer).getTies())));
					else
						map.put(redPlayer, new Data(redPlayer, 3, 1, 0, 0));

					if (!map.containsKey(whitePlayer))
						map.put(whitePlayer, new Data(whitePlayer, 0, 0, 1, 0));
					else
						map.replace(whitePlayer,
								new Data(whitePlayer, (map.get(whitePlayer).getPoints()),
										(map.get(whitePlayer).getWins()), (map.get(whitePlayer).getDefeats()+1),
										(map.get(whitePlayer).getTies())));
				} else {
					if (map.containsKey(redPlayer))
						map.replace(redPlayer,
								new Data(redPlayer, (map.get(redPlayer).getPoints() + 1),
										(map.get(redPlayer).getWins()), (map.get(redPlayer).getDefeats()),
										(map.get(redPlayer).getTies()+1)));
					else
						map.put(redPlayer, new Data(redPlayer, 1, 0,0 , 1));
					if (map.containsKey(whitePlayer))
						map.replace(whitePlayer,
								new Data(whitePlayer, (map.get(whitePlayer).getPoints()+1),
										(map.get(whitePlayer).getWins()), (map.get(whitePlayer).getDefeats()),
										(map.get(whitePlayer).getTies()+1)));
					else
						map.put(whitePlayer, new Data(whitePlayer, 1, 0, 0, 1));
				}
			}
		});
		map.forEach((k, v) -> {
			gamesList.add(v);
		});
	}
	private void sort() {
		Comparator<Data> comparator = Comparator.comparingDouble(Data::getPoints);
    		comparator = comparator.reversed();
    		gamesList.sort(comparator);
	}
}
