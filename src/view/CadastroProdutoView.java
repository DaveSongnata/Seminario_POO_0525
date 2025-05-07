package view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.BorderFactory; //basicamente um padding do css

import controller.Caixa;
import model.Produto;


public class CadastroProdutoView extends JFrame {
    private Caixa caixa;
    
    private JTextField txtCodigo;
    private JTextField txtNome;
    private JTextField txtPreco;
    private JTextField txtEstoque;
    private JTable tblProdutos;
    private DefaultTableModel modeloTabela;
    
    /**
     * Construtor que inicializa a interface
     */
    public CadastroProdutoView(Caixa caixa) {
        this.caixa = caixa;
        
        // Configurações da janela
        setTitle("Cadastro de Produtos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        //centraliza no meio da tela (pois não passamos um componente, passamos null)
        setLocationRelativeTo(null);
        
        // Inicializar componentes
        inicializarComponentes();
        
        // Mostrar janela
        setVisible(true);
    }
    
    /**
     * Inicializa todos os componentes da interface
     */
    private void inicializarComponentes() {
        // Painel de formulário
        JPanel painelFormulario = new JPanel(new GridLayout(5, 2, 10, 10));
        painelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 10, 5, 10));

        // Componentes do formulário
        painelFormulario.add(new JLabel("Código:"));
        txtCodigo = new JTextField(10);
        painelFormulario.add(txtCodigo);
        
        painelFormulario.add(new JLabel("Nome:"));
        txtNome = new JTextField(20);
        painelFormulario.add(txtNome);
        
        painelFormulario.add(new JLabel("Preço:"));
        txtPreco = new JTextField(10);
        painelFormulario.add(txtPreco);
        
        painelFormulario.add(new JLabel("Estoque:"));
        txtEstoque = new JTextField(10);
        painelFormulario.add(txtEstoque);
        
        // Botões
        JButton btnSalvar = new JButton("Salvar");
        JButton btnLimpar = new JButton("Limpar");
        
        // Eventos dos botões
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarProduto();
            }
        });
        
        btnLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limparCampos();
            }
        });
        
        JPanel painelBotoes = new JPanel();
        painelBotoes.add(btnSalvar);
        painelBotoes.add(btnLimpar);
        
        // Adiciona no painel
        painelFormulario.add(new JLabel(""));
        painelFormulario.add(painelBotoes);
        
        // Tabela de produtos
        String[] colunas = {"Código", "Nome", "Preço", "Estoque"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tblProdutos = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tblProdutos);
        
        // Layout principal
        setLayout(new BorderLayout(10, 10));
        add(painelFormulario, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Carregar produtos
        atualizarTabela();
    }
    
    /**
     * Salva um novo produto
     */
    private void salvarProduto() {
        double preco;
        int estoque;

        // Validações
        if (txtCodigo.getText().trim().isEmpty() || 
            txtNome.getText().trim().isEmpty() || 
            txtPreco.getText().trim().isEmpty() || 
            txtEstoque.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos são obrigatórios!",
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            preco = Double.parseDouble(txtPreco.getText().trim());
            if (preco <= 0) {
                JOptionPane.showMessageDialog(this, "O preço deve ser maior que zero!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Preço inválido!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            estoque = Integer.parseInt(txtEstoque.getText().trim());
            if (estoque < 0) {
                JOptionPane.showMessageDialog(this, "O estoque não pode ser negativo!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Estoque inválido!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Criar o produto
        Produto novoProduto = new Produto(
                txtCodigo.getText().trim(),
                txtNome.getText().trim(),
                preco,
                estoque
        );
        
        // Salvar no controller
        boolean cadastrado = caixa.cadastrarProduto(novoProduto);
        if (!cadastrado) {
            JOptionPane.showMessageDialog(this, "Já existe um produto com esse código!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Atualizar tabela
        atualizarTabela();
        
        // Limpar campos
        limparCampos();
        
        JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!", 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Limpa os campos do formulário
     */
    private void limparCampos() {
        txtCodigo.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        txtEstoque.setText("");
        txtCodigo.requestFocus();
    }
    
    /**
     * Atualiza a tabela com os produtos cadastrados
     */
    private void atualizarTabela() {
        // Limpar tabela
        modeloTabela.setRowCount(0);
        
        // Adicionar produtos
        for (Produto p : caixa.getProdutos()) {
            Object[] row = {
                p.getCodigo(),
                p.getNome(),
                p.getPreco(),
                p.getEstoque()
            };
            modeloTabela.addRow(row);
        }
    }
} 