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
    // autor
    @FXML
    private TextField autor;
    @FXML
    private TextField nacionalidade;
    // editora
    @FXML
    private TextField editora;
    @FXML
    private TextField siteeditora;
    @FXML
    private TextField paiseditora;
    // categoria
    @FXML
    private TextField categoria;
    // livro
    @FXML
    private TextField titulo;
    @FXML
    private TextField anoedicao;
    @FXML
    private TextField isbn;
    // autoria
    @FXML
    private TextField primeiraversao;
    // pesquisa
    @FXML
    private TextField pesquisa;

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
            String sql5 = "INSERT INTO autoria(ID_autor,ID_livro,ano_da_primeira_versão) VALUES (?,?,?)";

            // Cria um PreparedStatement para a consulta SQL
            PreparedStatement autorPreparedStatement = connection.prepareStatement(sql,
                    Statement.RETURN_GENERATED_KEYS);
            PreparedStatement editoraPreparedStatement = connection.prepareStatement(sql2,
                    Statement.RETURN_GENERATED_KEYS);
            PreparedStatement categoriaPreparedStatement = connection.prepareStatement(sql3,
                    Statement.RETURN_GENERATED_KEYS);
            PreparedStatement livroPreparedStatement = connection.prepareStatement(sql4,
                    Statement.RETURN_GENERATED_KEYS);
            PreparedStatement autoriaPreparedStatement = connection.prepareStatement(sql5,
                    Statement.RETURN_GENERATED_KEYS);
            // autor
            String V_Autor = autor.getText();
            String V_Nacionalidade = nacionalidade.getText();

            autorPreparedStatement.setString(1, V_Autor);
            autorPreparedStatement.setString(2, V_Nacionalidade);

            // editora
            String V_editora = editora.getText();
            String V_siteeditora = siteeditora.getText();
            String V_paiseditora = paiseditora.getText();

            editoraPreparedStatement.setString(1, V_editora);
            editoraPreparedStatement.setString(2, V_siteeditora);
            editoraPreparedStatement.setString(3, V_paiseditora);
            // categoria
            String V_categoria = categoria.getText();

            categoriaPreparedStatement.setString(1, V_categoria);
            // livro
            String V_titulo = titulo.getText();
            String V_anoedicao = anoedicao.getText();
            String V_isbn = isbn.getText();

            livroPreparedStatement.setString(1, V_titulo);
            livroPreparedStatement.setString(2, V_anoedicao);
            livroPreparedStatement.setString(3, V_isbn);
            // autoria
            String V_primeiraversao = primeiraversao.getText();

            autoriaPreparedStatement.setString(3, V_primeiraversao);
            // Insere os dados
            int linhasAfetadas = autorPreparedStatement.executeUpdate();
            int linhasAfetadas2 = editoraPreparedStatement.executeUpdate();
            int linhasAfetadas3 = categoriaPreparedStatement.executeUpdate();
            // pegar as chaves estrangeiras
            ResultSet V_idautor = autorPreparedStatement.getGeneratedKeys();
            long autorID = -1;
            if (V_idautor.next()) {
                autorID = V_idautor.getLong(1);
            }
            livroPreparedStatement.setLong(4, autorID);

            ResultSet V_idedi = editoraPreparedStatement.getGeneratedKeys();
            long ediID = -1;
            if (V_idedi.next()) {
                ediID = V_idedi.getLong(1);
            }
            livroPreparedStatement.setLong(5, ediID);

            ResultSet V_idcat = categoriaPreparedStatement.getGeneratedKeys();
            long catID = -1;
            if (V_idcat.next()) {
                catID = V_idcat.getLong(1);
            }
            livroPreparedStatement.setLong(6, catID);

            // //insere os dados do livro
            int linhasAfetadas4 = livroPreparedStatement.executeUpdate();
            // chaves estrangeiras autoria
            autoriaPreparedStatement.setLong(1, autorID);

            ResultSet V_idautoria_livro = livroPreparedStatement.getGeneratedKeys();
            long autoriaID2 = -1;
            if (V_idautoria_livro.next()) {
                autoriaID2 = V_idautoria_livro.getLong(1);
            }
            autoriaPreparedStatement.setLong(2, autoriaID2);
            // insere os dados
            int linhasAfetadas5 = autoriaPreparedStatement.executeUpdate();

            if (linhasAfetadas > 0 && linhasAfetadas2 > 0 && linhasAfetadas3 > 0 && linhasAfetadas4 > 0
                    && linhasAfetadas5 > 0) {
                int total = linhasAfetadas + linhasAfetadas2 + linhasAfetadas3 + linhasAfetadas4;
                System.out.println("Inserção bem-sucedida! " + total + " linha(s) inserida(s).");
                result.setText("");
                result.setText("Sucesso");
            } else {
                System.out.println("A inserção falhou.");
                result.setText("");
                result.setText("Erro");

            }
            // fecha a conexão
            autorPreparedStatement.close();
            categoriaPreparedStatement.close();
            livroPreparedStatement.close();
            editoraPreparedStatement.close();
            autoriaPreparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnResetClicked(ActionEvent event) {
        result.setText("");
        autor.setText("");
        nacionalidade.setText("");
        editora.setText("");
        siteeditora.setText("");
        paiseditora.setText("");
        categoria.setText("");
        titulo.setText("");
        anoedicao.setText("");
        isbn.setText("");
        primeiraversao.setText("");
    }
    
    @FXML
    void btnPesquisaClicked(ActionEvent event) {
        String jdbcUrl = "jdbc:mysql://127.0.0.1:3306/livro?user=root";
        String Username = "root";
        String password = "";
        String V_pesquisa = pesquisa.getText();
        System.out.println(V_pesquisa);
        try {
            // Conecta ao banco
            Connection connection = DriverManager.getConnection(jdbcUrl, Username, password);
            //Buscando informações do livro
            String sql = "SELECT titulo_livro,ISBN_livro,ano_da_edicao_livro FROM livro WHERE titulo_livro = ?";
            PreparedStatement livroPreparedStatement = connection.prepareStatement(sql);
            livroPreparedStatement.setString(1, V_pesquisa);
            ResultSet resultadolivro = livroPreparedStatement.executeQuery();
            System.out.println(resultadolivro);
            while (resultadolivro.next()) {
                String V_titulo = resultadolivro.getString("titulo_livro");
                String V_isbn = resultadolivro.getString("ISBN_livro");
                String V_anodaedicao = resultadolivro.getString("ano_da_edicao_livro");
                titulo.setText(V_titulo);
                isbn.setText(V_isbn);
                anoedicao.setText(V_anodaedicao);
            }

            //Buscando informações do autor
            // String sql2 = "SELECT nome,nacionalidade FROM autor WHERE";



            //autorPreparedStatement.close();
            //categoriaPreparedStatement.close();
            livroPreparedStatement.close();
            //editoraPreparedStatement.close();
            //autoriaPreparedStatement.close();
            connection.close();
        } catch (Exception e) {
        }

    }
}
