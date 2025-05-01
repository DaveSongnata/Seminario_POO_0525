package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.format.DateTimeFormatter;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import controller.Caixa;
import model.ItemVenda;
import model.Venda;

/**
 * Interface gráfica para gestão de vendas.
 * Esta classe permite visualizar, filtrar e cancelar vendas realizadas.
 */
public class GestaoVendasView extends JFrame {
    private Caixa caixa;
    
    private JCheckBox chkMostrarCanceladas;
    private JTable tblVendas;
    private DefaultTableModel modeloTabela;
    
    /**
     * Construtor que inicializa a interface
     */
    public GestaoVendasView(Caixa caixa) {
        this.caixa = caixa;
        
        // Configurações da janela
        setTitle("Gestão de Vendas");
        setSize(800, 500);
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
        // Painel superior para filtros
        JPanel painelFiltros = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        // Checkbox para mostrar vendas canceladas
        chkMostrarCanceladas = new JCheckBox("Mostrar vendas canceladas");
        chkMostrarCanceladas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                atualizarTabela();
            }
        });
        painelFiltros.add(chkMostrarCanceladas);
        
        // Tabela de vendas - Define colunas e torna células não editáveis
        String[] colunas = {"Número", "Data", "Total", "Status"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblVendas = new JTable(modeloTabela);
        
        // Configuração visual - Colorir vendas canceladas em vermelho
        tblVendas.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String status = (String) table.getValueAt(row, 3);
                if ("Cancelada".equals(status)) {
                    c.setForeground(Color.RED);
                } else {
                    c.setForeground(table.getForeground());
                }
                
                return c;
            }
        });
        
        // Adiciona a tabela a um painel com scroll
        JScrollPane scrollPane = new JScrollPane(tblVendas);
        
        // Painel inferior com botões
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        // Botão para cancelar vendas
        JButton btnCancelar = new JButton("Cancelar Venda");
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelarVendaSelecionada();
            }
        });
        
        // Botão para ver detalhes da venda
        JButton btnDetalhes = new JButton("Detalhes");
        btnDetalhes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarDetalhesVenda();
            }
        });
        
        // Adiciona botões ao painel
        painelBotoes.add(btnCancelar);
        painelBotoes.add(btnDetalhes);
        
        // Organiza os componentes na janela
        setLayout(new BorderLayout(10, 10));
        add(painelFiltros, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
        
        // Carrega vendas iniciais
        atualizarTabela();
    }
    
    /**
     * Atualiza a tabela de vendas com base nos filtros
     */
    private void atualizarTabela() {
        // Limpar tabela
        modeloTabela.setRowCount(0);
        
        // Formatador para exibir a data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        // Adicionar vendas que atendem ao filtro
        for (Venda venda : caixa.getVendas()) {
            // Filtrar vendas canceladas, se não estiver marcado
            if (venda.isCancelada() && !chkMostrarCanceladas.isSelected()) {
                continue;
            }
            
            String status = venda.isCancelada() ? "Cancelada" : "Concluída";
            
            // Adiciona a venda como uma nova linha na tabela
            Object[] row = {
                venda.getNumero(),
                venda.getData().format(formatter),
                String.format("R$ %.2f", venda.calcularTotal()),
                status
            };
            modeloTabela.addRow(row);
        }
    }
    
    /**
     * Cancela a venda selecionada na tabela
     */
    private void cancelarVendaSelecionada() {
        // Verifica se há uma linha selecionada
        int linhaSelecionada = tblVendas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma venda para cancelar!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Verifica se a venda já está cancelada
        String status = (String) tblVendas.getValueAt(linhaSelecionada, 3);
        if ("Cancelada".equals(status)) {
            JOptionPane.showMessageDialog(this, "Esta venda já está cancelada!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Obtém o número da venda selecionada
        String numeroVenda = (String) tblVendas.getValueAt(linhaSelecionada, 0);
        
        // Confirmação de cancelamento
        int confirmacao = JOptionPane.showConfirmDialog(this, 
                "Tem certeza que deseja cancelar a venda " + numeroVenda + "?", 
                "Confirmação", JOptionPane.YES_NO_OPTION);
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            // Executa o cancelamento
            caixa.cancelarVenda(numeroVenda);
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Venda cancelada com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Mostra os detalhes da venda selecionada
     */
    private void mostrarDetalhesVenda() {
        // Verifica se há uma linha selecionada
        int linhaSelecionada = tblVendas.getSelectedRow();
        if (linhaSelecionada == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma venda para ver detalhes!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Obtém o número da venda selecionada
        String numeroVenda = (String) tblVendas.getValueAt(linhaSelecionada, 0);
        
        // Buscar venda pelo número
        Venda vendaSelecionada = null;
        for (Venda v : caixa.getVendas()) {
            if (v.getNumero().equals(numeroVenda)) {
                vendaSelecionada = v;
                break;
            }
        }
        
        if (vendaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Venda não encontrada!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Cria uma nova janela para mostrar os detalhes
        JFrame janelaDetalhes = new JFrame("Detalhes da Venda " + numeroVenda);
        janelaDetalhes.setSize(600, 400);
        janelaDetalhes.setLocationRelativeTo(this);
        janelaDetalhes.setLayout(new BorderLayout(10, 10));
        
        // Tabela de itens da venda
        String[] colunas = {"Produto", "Preço Unit.", "Quantidade", "Subtotal"};
        DefaultTableModel modeloDetalhes = new DefaultTableModel(colunas, 0);
        JTable tblDetalhes = new JTable(modeloDetalhes);
        
        // Preenche a tabela com os itens da venda
        for (ItemVenda item : vendaSelecionada.getItens()) {
            Object[] row = {
                item.getProduto().getNome(),
                String.format("R$ %.2f", item.getProduto().getPreco()),
                item.getQuantidade(),
                String.format("R$ %.2f", item.getValorTotal())
            };
            modeloDetalhes.addRow(row);
        }
        
        JScrollPane scrollPane = new JScrollPane(tblDetalhes);
        
        // Painel com informações gerais da venda
        JPanel painelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        painelInfo.add(new JLabel("Número: " + vendaSelecionada.getNumero()));
        painelInfo.add(new JLabel(" | Data: " + vendaSelecionada.getData().format(formatter)));
        painelInfo.add(new JLabel(" | Status: " + (vendaSelecionada.isCancelada() ? "Cancelada" : "Concluída")));
        painelInfo.add(new JLabel(" | Total: R$ " + String.format("%.2f", vendaSelecionada.calcularTotal())));
        
        // Adiciona os componentes à janela
        janelaDetalhes.add(painelInfo, BorderLayout.NORTH);
        janelaDetalhes.add(scrollPane, BorderLayout.CENTER);
        
        // Exibe a janela
        janelaDetalhes.setVisible(true);
    }
} 