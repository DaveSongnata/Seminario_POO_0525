package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import controller.Caixa;
import model.ItemVenda;
import model.Produto;
import model.Venda;

/**
 * Interface gráfica para o ponto de venda.
 */
public class PontoVendaView extends JFrame {
    private Caixa caixa;
    private Venda vendaAtual;
    
    private JComboBox<Produto> cbProdutos;
    private JSpinner spQuantidade;
    private JTable tblItens;
    private DefaultTableModel modeloTabela;
    private JLabel lblTotal;
    
    /**
     * Construtor que inicializa a interface
     */
    public PontoVendaView(Caixa caixa) {
        this.caixa = caixa;
        this.vendaAtual = new Venda();
        
        // Configurações da janela
        setTitle("Ponto de Venda");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        // Painel superior para seleção de produtos
        JPanel painelSuperior = new JPanel(new GridLayout(2, 2, 10, 10));
        
        // ComboBox para produtos
        painelSuperior.add(new JLabel("Produto:"));
        cbProdutos = new JComboBox<>();
        atualizarComboBoxProdutos();
        painelSuperior.add(cbProdutos);
        
        // Spinner para quantidade
        painelSuperior.add(new JLabel("Quantidade:"));
        spQuantidade = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        painelSuperior.add(spQuantidade);
        
        // Botão para adicionar item
        JButton btnAdicionar = new JButton("Adicionar Item");
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarItem();
            }
        });
        
        // Tabela de itens
        String[] colunas = {"Produto", "Preço Unit.", "Quantidade", "Subtotal"};
        modeloTabela = new DefaultTableModel(colunas, 0);
        tblItens = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tblItens);
        
        // Painel inferior
        JPanel painelInferior = new JPanel(new BorderLayout(10, 10));
        JPanel painelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        lblTotal = new JLabel("Total: R$ 0,00");
        painelTotal.add(lblTotal);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton btnFinalizar = new JButton("Finalizar Venda");
        btnFinalizar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalizarVenda();
            }
        });
        
        JButton btnCancelar = new JButton("Cancelar Venda");
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarVenda();
            }
        });
        
        painelBotoes.add(btnFinalizar);
        painelBotoes.add(btnCancelar);
        
        painelInferior.add(painelTotal, BorderLayout.NORTH);
        painelInferior.add(painelBotoes, BorderLayout.SOUTH);
        
        // Layout principal
        setLayout(new BorderLayout(10, 10));
        JPanel painelAdicionarItem = new JPanel(new BorderLayout());
        painelAdicionarItem.add(painelSuperior, BorderLayout.CENTER);
        painelAdicionarItem.add(btnAdicionar, BorderLayout.EAST);
        
        add(painelAdicionarItem, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(painelInferior, BorderLayout.SOUTH);
    }
    
    /**
     * Atualiza o ComboBox com a lista de produtos
     */
    private void atualizarComboBoxProdutos() {
        DefaultComboBoxModel<Produto> model = new DefaultComboBoxModel<>();
        
        // Adicionar apenas produtos com estoque
        for (Produto p : caixa.getProdutos()) {
            if (p.getEstoque() > 0) {
                model.addElement(p);
            }
        }
        
        cbProdutos.setModel(model);
        
        // Customizar exibição
        cbProdutos.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList<?> list, Object value, int index, 
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Produto) {
                    Produto produto = (Produto) value;
                    setText(produto.getCodigo() + " - " + produto.getNome() + 
                            " (R$ " + produto.getPreco() + ") - Estoque: " + produto.getEstoque());
                }
                return this;
            }
        });
    }
    
    /**
     * Adiciona um item à venda
     */
    private void adicionarItem() {
        Produto produtoSelecionado = (Produto) cbProdutos.getSelectedItem();
        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um produto!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int quantidade = (int) spQuantidade.getValue();
        
        if (quantidade > produtoSelecionado.getEstoque()) {
            JOptionPane.showMessageDialog(this, 
                    "Quantidade selecionada maior que o estoque disponível!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Adicionar à venda
        vendaAtual.adicionarItem(produtoSelecionado, quantidade);
        
        // Atualizar tabela
        atualizarTabelaItens();
        
        // Atualizar total
        atualizarTotal();
        
        // Atualizar combo box (pois estoque pode ter mudado)
        atualizarComboBoxProdutos();
    }
    
    /**
     * Atualiza a tabela de itens
     */
    private void atualizarTabelaItens() {
        // Limpar tabela
        modeloTabela.setRowCount(0);
        
        // Adicionar itens
        for (ItemVenda item : vendaAtual.getItens()) {
            Object[] row = {
                item.getProduto().getNome(),
                item.getProduto().getPreco(),
                item.getQuantidade(),
                item.getValorTotal()
            };
            modeloTabela.addRow(row);
        }
    }
    
    /**
     * Atualiza o valor total da venda
     */
    private void atualizarTotal() {
        double total = vendaAtual.calcularTotal();
        lblTotal.setText(String.format("Total: R$ %.2f", total));
    }
    
    /**
     * Finaliza a venda atual
     */
    private void finalizarVenda() {
        if (vendaAtual.getItens().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não há itens na venda atual!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Registrar a venda no caixa
        boolean realizada = caixa.realizarVenda(vendaAtual);
        if (!realizada) {
            JOptionPane.showMessageDialog(this, "Estoque insuficiente para um ou mais produtos!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JOptionPane.showMessageDialog(this, 
                "Venda finalizada com sucesso!\nNúmero: " + vendaAtual.getNumero(), 
                "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        
        // Iniciar nova venda
        vendaAtual = new Venda();
        atualizarTabelaItens();
        atualizarTotal();
        atualizarComboBoxProdutos();
    }
    
    /**
     * Cancela a venda atual
     */
    private void cancelarVenda() {
        // Reiniciar venda
        vendaAtual = new Venda();
        atualizarTabelaItens();
        atualizarTotal();
        atualizarComboBoxProdutos();
        
        JOptionPane.showMessageDialog(this, "Venda cancelada!", 
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
    }
} 