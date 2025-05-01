import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import controller.Caixa;
import model.CodigoDuplicadoException;
import model.Produto;
import view.CadastroProdutoView;
import view.GestaoVendasView;
import view.PontoVendaView;

public class Main {
    private static Caixa caixa;

    public static void main(String[] args) {
        iniciarSistema();
    }

    private static void iniciarSistema() {
        caixa = new Caixa();
        adicionarProdutosExemplo();

        JFrame janela = new JFrame("Menu Principal");

        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /* é preciso pq Sem ela:
O comportamento padrão de um JFrame é:
HIDE_ON_CLOSE
       */


        JPanel painel = new JPanel();


        JButton btnCadastro = new JButton("Cadastro de Produtos");
        btnCadastro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new CadastroProdutoView(caixa);
            }
        });

        JButton btnVenda = new JButton("Ponto de Venda");
        btnVenda.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new PontoVendaView(caixa);
            }
        });

        JButton btnGestao = new JButton("Gestão de Vendas");
        btnGestao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new GestaoVendasView(caixa);
            }
        });

        painel.add(btnCadastro);
        painel.add(btnVenda);
        painel.add(btnGestao);

        janela.add(painel);
        janela.pack(); // Ajusta automaticamente o tamanho da janela aos componentes
        janela.setLocationRelativeTo(null); // Centraliza na tela
        janela.setVisible(true);
    }

    private static void adicionarProdutosExemplo() {
        try {
            caixa.cadastrarProduto(new Produto("001", "Arroz 5Kg", 25.90, 10));
            caixa.cadastrarProduto(new Produto("002", "Feijão 1Kg", 8.90, 20));
            caixa.cadastrarProduto(new Produto("003", "Café 500g", 12.50, 15));
            caixa.cadastrarProduto(new Produto("004", "Óleo de Soja 900ml", 7.80, 30));
            caixa.cadastrarProduto(new Produto("005", "Açúcar 2Kg", 9.70, 25));
        } catch (CodigoDuplicadoException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro ao cadastrar produtos: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
