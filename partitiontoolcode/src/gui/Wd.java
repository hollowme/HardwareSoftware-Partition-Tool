package gui;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.border.LineBorder;
import java.awt.Color;

public class Wd extends JFrame {

	private JPanel contentPane;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_4;

	// 画图面板
	JPanel panel = null;
	JFreeChart freeChart=null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Wd frame = new Wd();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Wd() {

		PartitionToolCore ptc = new PartitionToolCore();

		setTitle("Hardware/Software Partition Tool");
		// initiate the window
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 813, 560);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// file content
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(27, 62, 359, 245);
		contentPane.add(scrollPane_1);

		JTextArea textArea_1 = new JTextArea();
		textArea_1.setLineWrap(true);
		textArea_1.setText("\u9009\u62E9\u6587\u4EF6\u7684\u4FE1\u606F\u5C06\u663E\u793A"
				+ "\u5728\u8FD9\u91CC\u3002\r\n\u6587\u4EF6\u5305\u542B\u4E09\u90E8\u5206"
				+ "\uFF1A\r\n1. \u4EFB\u52A1\u6570\u91CFN\r\n2. N*N\u7684\u901A\u4FE1\u77E9"
				+ "\u9635\uFF0C\u540C\u4E00\u884C\u7684\u5143\u7D20\u7528\u7A7A\u683C\u9694"
				+ "\u5F00\uFF0C\u4E0D\u540C\u884C\u7684\u5143\u7D20\u7528\u56DE\u8F66\u9694"
				+ "\u5F00\r\n3. N*5\u7684\u4EFB\u52A1\u5C5E\u6027\u8868\uFF0C\u6BCF\u5217"
				+ "\u5206\u522B\u4E3A\u8F6F\u4EF6\u5F00\u9500\uFF0C\u8F6F\u4EF6\u6267\u884C"
				+ "\u65F6\u95F4\uFF0C\u786C\u4EF6\u5F00\u9500\uFF0C\u786C\u4EF6\u8FD0\u884C"
				+ "\u65F6\u95F4\uFF0C\u786C\u4EF6\u9762\u79EF");
		scrollPane_1.setViewportView(textArea_1);

		// result text
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(432, 373, 349, 129);
		contentPane.add(scrollPane);

		JTextArea textArea = new JTextArea();
		scrollPane.setViewportView(textArea);

		// statistic panel
		freeChart = ChartFactory.createBarChart(null, // 图表标题
				"Compare", // 水平轴的显示标签
				null, // 垂直轴的显示标签
				null, // 数据集，即要显示在图表上的数据
				PlotOrientation.VERTICAL, // 图表方向：水平，垂直
				true, // 是否显示图例
				false, // 是否显示提示
				false// 是否生成URL连接
		);
		panel = new ChartPanel(freeChart);
		panel.setBounds(432, 62, 349, 291);
		contentPane.add(panel);

		// labels
		JLabel lblFileName = new JLabel("\u6587\u4EF6\u540D");
		lblFileName.setBounds(27, 27, 72, 18);
		contentPane.add(lblFileName);

		JLabel lblNewLabel = new JLabel("\u6A21\u5757\u6570");
		lblNewLabel.setBounds(36, 335, 62, 18);
		contentPane.add(lblNewLabel);

		JLabel label_1 = new JLabel("\u6C42\u89E3\u76EE\u6807");
		label_1.setBounds(34, 376, 72, 18);
		contentPane.add(label_1);

		JLabel label = new JLabel("\u65F6\u95F4\u7EA6\u675F");
		label.setBounds(34, 414, 72, 18);
		contentPane.add(label);

		JLabel lblNewLabel_1 = new JLabel("\u5F00\u9500\u7EA6\u675F");
		lblNewLabel_1.setBounds(159, 414, 72, 18);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("\u786C\u4EF6\u7EA6\u675F");
		lblNewLabel_2.setBounds(290, 414, 72, 18);
		contentPane.add(lblNewLabel_2);

		JLabel lblOutput = new JLabel("\u7ED3\u679C\u7EDF\u8BA1\u548C\u5206\u6790");
		lblOutput.setBounds(432, 27, 131, 18);
		contentPane.add(lblOutput);

		textField_1 = new JTextField();
		textField_1.setBounds(83, 25, 131, 24);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		textField = new JTextField();
		textField.setBounds(100, 329, 86, 24);
		contentPane.add(textField);
		textField.setColumns(10);

		textField_2 = new JTextField();
		textField_2.setBounds(159, 432, 86, 24);
		contentPane.add(textField_2);
		textField_2.setColumns(10);

		textField_3 = new JTextField();
		textField_3.setBounds(290, 432, 86, 24);
		contentPane.add(textField_3);
		textField_3.setColumns(10);

		textField_4 = new JTextField();
		textField_4.setBounds(34, 432, 86, 24);
		contentPane.add(textField_4);
		textField_4.setColumns(10);

		// read button
		JButton btnNewButton_1 = new JButton("\u8BFB\u53D6");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// 读取按钮的动作
				ptc.filename = textField_1.getText();
				ptc.getContent();
				textArea_1.setText(ptc.printContent());
			}
		});

		JComboBox<String> comboBox = new JComboBox<String>();
		comboBox.setBounds(101, 373, 285, 24);
		contentPane.add(comboBox);
		btnNewButton_1.setBounds(273, 23, 113, 27);
		contentPane.add(btnNewButton_1);
		comboBox.addItem("在系统开销和硬件面积约束下，系统运行时间最短");
		comboBox.addItem("在系统运行时间和硬件面积约束下，系统开销最小");
		comboBox.addItem("在系统运行时间约束下，硬件面积最小");

		// run button，负责传入算法参数，打印结果和画柱状图
		JButton btnNewButton = new JButton("\u6C42\u89E3");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {// 运行按钮的动作
				//传入模块数和约束参数
				int modulenumber = Integer.parseInt(textField.getText());
				double costconstr = Double.parseDouble(textField_2.getText());
				int areaconstr = Integer.parseInt(textField_3.getText());
				int timeconstr = Integer.parseInt(textField_4.getText());
				ptc.getConstr(modulenumber, costconstr, areaconstr, timeconstr);
				
				//获取运行结果用于绘制柱状图
				int[] commresult=new int[5];
				double[] targetresult=new double[5];

				if (comboBox.getSelectedItem() == "在系统开销和硬件面积约束下，系统运行时间最短") {
					//获取打印字符串和画图信息
					textArea.setText(ptc.run(1,commresult,targetresult));
					
					//画图
					DefaultCategoryDataset dataset = new DefaultCategoryDataset();
					//dataset.addValue(commresult[0]/10, "commcost", "MMM1");
					dataset.addValue(targetresult[0], "runtime", "MMM1");
					//dataset.addValue(commresult[1]/10, "commcost", "MMM2");
					dataset.addValue(targetresult[1], "runtime", "MMM2");
					//dataset.addValue(commresult[2]/10, "commcost", "MMM3");
					dataset.addValue(targetresult[2], "runtime", "MMM3");
					//dataset.addValue(commresult[3]/10, "commcost", "KL");
					dataset.addValue(targetresult[3], "runtime", "KL");
					//dataset.addValue(commresult[4]/10, "commcost", "Mod");
					dataset.addValue(targetresult[4], "runtime", "Mod");
					//绘制柱状图
					freeChart = ChartFactory.createBarChart(null, // 图表标题
							"Running Time Compare", // 水平轴的显示标签
							null, // 垂直轴的显示标签
							dataset, // 数据集，即要显示在图表上的数据
							PlotOrientation.VERTICAL, // 图表方向：水平，垂直
							false, // 是否显示图例
							false, // 是否显示提示
							false// 是否生成URL连接
					);
					panel = new ChartPanel(freeChart);
					panel.setBounds(432, 62, 349, 291);
					contentPane.add(panel);
					panel.repaint();
					
				} else if (comboBox.getSelectedItem() == "在系统运行时间和硬件面积约束下，系统开销最小") {
					textArea.setText(ptc.run(2,commresult,targetresult));
					
					//画图
					DefaultCategoryDataset dataset = new DefaultCategoryDataset();
					//dataset.addValue(commresult[0]/10, "commcost", "MMM1");
					dataset.addValue(targetresult[0], "syscost", "MMM1");
					//dataset.addValue(commresult[1]/10, "commcost", "MMM2");
					dataset.addValue(targetresult[1], "syscost", "MMM2");
					//dataset.addValue(commresult[2]/10, "commcost", "MMM3");
					dataset.addValue(targetresult[2], "syscost", "MMM3");
					//dataset.addValue(commresult[3]/10, "commcost", "KL");
					dataset.addValue(targetresult[3], "syscost", "KL");
					//dataset.addValue(commresult[4]/10, "commcost", "Mod");
					dataset.addValue(targetresult[4], "syscost", "Mod");
					//绘制柱状图
					freeChart = ChartFactory.createBarChart(null, // 图表标题
							"System Cost Compare", // 水平轴的显示标签
							null, // 垂直轴的显示标签
							dataset, // 数据集，即要显示在图表上的数据
							PlotOrientation.VERTICAL, // 图表方向：水平，垂直
							false, // 是否显示图例
							false, // 是否显示提示
							false// 是否生成URL连接
					);
					panel = new ChartPanel(freeChart);
					panel.setBounds(432, 62, 349, 291);
					contentPane.add(panel);
					panel.repaint();
					
				} else if (comboBox.getSelectedItem() == "在系统运行时间约束下，硬件面积最小") {
					textArea.setText(ptc.run(3,commresult,targetresult));
					
					//画图
					DefaultCategoryDataset dataset = new DefaultCategoryDataset();
					//dataset.addValue(commresult[0]/10, "commcost", "MMM1");
					dataset.addValue(targetresult[0], "sysarea", "MMM1");
					//dataset.addValue(commresult[1]/10, "commcost", "MMM2");
					dataset.addValue(targetresult[1], "sysarea", "MMM2");
					//dataset.addValue(commresult[2]/10, "commcost", "MMM3");
					dataset.addValue(targetresult[2], "sysarea", "MMM3");
					//dataset.addValue(commresult[3]/10, "commcost", "KL");
					dataset.addValue(targetresult[3], "sysarea", "KL");
					//dataset.addValue(commresult[4]/10, "commcost", "Mod");
					dataset.addValue(targetresult[4], "sysarea", "Mod");
					//绘制柱状图
					freeChart = ChartFactory.createBarChart(null, // 图表标题
							"System Area Compare", // 水平轴的显示标签
							null, // 垂直轴的显示标签
							dataset, // 数据集，即要显示在图表上的数据
							PlotOrientation.VERTICAL, // 图表方向：水平，垂直
							false, // 是否显示图例
							false, // 是否显示提示
							false// 是否生成URL连接
					);
					panel = new ChartPanel(freeChart);
					panel.setBounds(432, 62, 349, 291);
					contentPane.add(panel);
					panel.repaint();
				}
			}
		});

		// show the communication matrix button
		JButton button = new JButton("\u67E5\u770B\u901A\u4FE1\u77E9\u9635");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new MatrixFrame(ptc.printCommMatrix());
			}
		});
		button.setBounds(255, 328, 131, 27);
		contentPane.add(button);
		btnNewButton.setBounds(142, 475, 113, 27);
		contentPane.add(btnNewButton);

	}
}
