import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MainSceneController {
    @FXML
    private Label result;
    //autor
    @FXML
    private TextField autor;
    @FXML
    private TextField nacionalidade;
    //editora
    @FXML
    private TextField editora;
    @FXML
    private TextField siteeditora;
    @FXML
    private TextField paiseditora;
    //categoria
    @FXML
    private TextField categoria;
    //livro
    @FXML
    private TextField titulo;
    @FXML
    private TextField anoedicao;
    @FXML
    private TextField isbn;
    //autoria
    @FXML
    private TextField primeiraversao;

    @FXML
    void btnSendClicked(ActionEvent event) {
        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/livro?user=root";
        String Username = "root";
        String password = "";
        try {
            // Conecta ao banco
            Connection connection = DriverManager.getConnection(jdbcUrl, Username, password);

            // executa comandos
            String sql = "INSERT INTO autor(nome,nacionalidade) VALUES (?,?)";
            String sql2 = "INSERT INTO editora(nome_editora,site_editora,pais_de_origem) VALUES (?,?,?)";
            String sql3 = "INSERT INTO categoria(desc_categoria) VALUES (?)";
            String sql4 = "INSERT INTO livro(titulo_livro,ano_da_edicao_livro,ISBN_livro,ID_autor,ID_editora,ID_categoria) VALUES (?,?,?,?,?,?)";

            // Cria um PreparedStatement para a consulta SQL
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2,Statement.RETURN_GENERATED_KEYS);
            PreparedStatement preparedStatement3 = connection.prepareStatement(sql3,Statement.RETURN_GENERATED_KEYS);
            PreparedStatement preparedStatement4 = connection.prepareStatement(sql4,Statement.RETURN_GENERATED_KEYS);
            //autor
            String V_Autor = autor.getText();
            String V_Nacionalidade = nacionalidade.getText();

            preparedStatement.setString(1, V_Autor);
            preparedStatement.setString(2, V_Nacionalidade);

            //editora
            String V_editora = editora.getText();
            String V_siteeditora = siteeditora.getText();
            String V_paiseditora = paiseditora.getText();

            preparedStatement2.setString(1, V_editora);
            preparedStatement2.setString(2, V_siteeditora);
            preparedStatement2.setString(3, V_paiseditora);
            //categoria
            String V_categoria = categoria.getText();

            preparedStatement3.setString(1, V_categoria);
            //livro
            String V_titulo = titulo.getText();
            String V_anoedicao = anoedicao.getText();
            String V_isbn = isbn.getText();


            preparedStatement4.setString(1, V_titulo);
            preparedStatement4.setString(2, V_anoedicao);
            preparedStatement4.setString(3, V_isbn);
            // Insere os dados
            int linhasAfetadas = preparedStatement.executeUpdate();
            int linhasAfetadas2 = preparedStatement2.executeUpdate();
            int linhasAfetadas3 = preparedStatement3.executeUpdate();
            //pegar as chaves estrangeiras
            ResultSet V_idautor = preparedStatement.getGeneratedKeys();
            long autorID = -1;
            if(V_idautor.next()){
                autorID = V_idautor.getLong(1);
            }
            preparedStatement4.setLong(4, autorID);
            
            ResultSet V_idedi = preparedStatement2.getGeneratedKeys();
            long ediID = -1;
            if(V_idedi.next()){
                ediID = V_idedi.getLong(1);
            }
            preparedStatement4.setLong(5, ediID);
            
            ResultSet V_idcat = preparedStatement3.getGeneratedKeys();
            long catID = -1;
            if(V_idcat.next()){
                catID = V_idcat.getLong(1);
            }
            preparedStatement4.setLong(6, catID);



            // //insere os dados do livro
            int linhasAfetadas4 = preparedStatement4.executeUpdate();

            if (linhasAfetadas > 0 && linhasAfetadas2 > 0 && linhasAfetadas3 > 0 && linhasAfetadas4 > 0) {
                int total = linhasAfetadas+linhasAfetadas2+linhasAfetadas3+linhasAfetadas4;
                System.out.println("Inserção bem-sucedida! " + total + " linha(s) inserida(s).");
                result.setText("");
                result.setText("Sucesso");
            } else {
                System.out.println("A inserção falhou.");
                result.setText("");
                result.setText("Erro");
                
            }
            // fecha a conexão
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void btnResetClicked(ActionEvent event){

    }
}
