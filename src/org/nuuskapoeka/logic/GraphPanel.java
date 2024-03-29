package org.nuuskapoeka.logic;

import org.nuuskapoeka.domain.Guild;
import org.nuuskapoeka.domain.Hero;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class GraphPanel extends JPanel {

    private int width = 800;
    private int heigth = 400;
    private int padding = 25;
    private int labelPadding = 25;
    private Color lineColor = new Color(44, 102, 230, 180);
    private Color altLineColor = new Color(230, 44, 44, 180);
    private Color pointColor = new Color(100, 100, 100, 180);
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);
    private int pointWidth = 4;
    private int numberYDivisions = 10;
    private List<Double> scores;
    private List<Double> compScores;
    private static Guild guild;

    private static JComboBox heroList;
    private static JFrame frame;

    private static boolean isComparison;

    public GraphPanel(List<Double> scores, Guild guild, JComboBox heroList) {
        this.scores = scores;
        this.guild = guild;
        this.heroList = heroList;
        compScores = new ArrayList<>();
    }

    public void setComparison(List<Double> scores){
        isComparison = true;
        compScores = scores;
    }

    @Override
    protected void paintComponent(Graphics g) {
        isComparison = false;
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double xScale = ((double) getWidth() - (2 * padding) - labelPadding) / (scores.size() - 1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (getMaxScore() - getMinScore());

        List<Point> graphPoints = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            int x1 = (int) (i * xScale + padding + labelPadding);
            int y1 = (int) ((getMaxScore() - scores.get(i)) * yScale + padding);
            graphPoints.add(new Point(x1, y1));
        }
        List<Point> graphPoints2 = new ArrayList<>();
        if(isComparison){
            for (int i = 0; i < compScores.size(); i++) {
                int x1 = (int) (i * xScale + padding + labelPadding);
                int y1 = (int) ((getMaxScore() - compScores.get(i)) * yScale + padding);
                graphPoints.add(new Point(x1, y1));
                graphPoints2.add(new Point(x1, y1));
            }
        }

        // draw white background
        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLACK);

        // create hatch marks and grid lines for y axis.
        for (int i = 0; i < numberYDivisions + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) / numberYDivisions + padding + labelPadding);
            int y1 = y0;
            if (scores.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = convertNumber((((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions))))) + "";
                //System.out.println((((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions)))) +", " + convertNumber((((getMinScore() + (getMaxScore() - getMinScore()) * ((i * 1.0) / numberYDivisions))))));
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        // and for x axis
        for (int i = 0; i < scores.size(); i++) {
            if (scores.size() > 1) {
                int x0 = i * (getWidth() - padding * 2 - labelPadding) / (scores.size() - 1) + padding + labelPadding;
                int x1 = x0;
                int y0 = getHeight() - padding - labelPadding;
                int y1 = y0 - pointWidth;
                if ((i % ((int) ((scores.size() / 20.0)) + 1)) == 0) {
                    g2.setColor(gridColor);
                    g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                    g2.setColor(Color.BLACK);
                    String xLabel = "label";
                    if(guild.getHero(heroList.getSelectedItem().toString()) != null){
                        xLabel = guild.getHero(heroList.getSelectedItem().toString()).getEvents().get(i) + "";
                    }else{
                        xLabel = guild.getEvents().get(i).toString();
                    }
                    FontMetrics metrics = g2.getFontMetrics();
                    int labelWidth = metrics.stringWidth(xLabel);
                    g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }

        // create x and y axes
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() - padding, getHeight() - padding - labelPadding);

        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        for (int i = 0; i < graphPoints.size() - 1; i++) {
            int x1 = graphPoints.get(i).x;
            int y1 = graphPoints.get(i).y;
            int x2 = graphPoints.get(i + 1).x;
            int y2 = graphPoints.get(i + 1).y;
            g2.drawLine(x1, y1, x2, y2);
        }


        if(isComparison){
            g2.setColor(altLineColor);
            g2.setStroke(GRAPH_STROKE);
            for (int i = 0; i < graphPoints2.size() - 1; i++) {
                int x1 = graphPoints2.get(i).x;
                int y1 = graphPoints2.get(i).y * 2;
                int x2 = graphPoints2.get(i + 1).x;
                int y2 = graphPoints2.get(i + 1).y * 2;
                g2.drawLine(x1, y1, x2, y2);
            }

        }

        g2.setStroke(oldStroke);
        g2.setColor(pointColor);
        for (int i = 0; i < graphPoints.size(); i++) {
            int x = graphPoints.get(i).x - pointWidth / 2;
            int y = graphPoints.get(i).y - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.fillOval(x, y, ovalW, ovalH);
        }
    }
    //    @Override
//    public Dimension getPreferredSize() {
//        return new Dimension(width, heigth);
//    }
    public String convertNumber(double number){
        if(number > 1000000000){
            return (double) Math.round((number/1000000000)*10)/10 + "b";
        }else if(number > 1000000){
            return (double)Math.round((number/1000000)*10)/10 + "m";

        }else if(number >1000){
            return (double)Math.round((number/1000)*100)/100 + "k";
        }
        return number + "";
    }
    private double getMinScore() {
        double minScore = Double.MAX_VALUE;
        for (Double score : scores) {
            minScore = Math.min(minScore, score);
        }
        return minScore/2;
    }

    private double getMaxScore() {
        double maxScore = Double.MIN_VALUE;
        for (Double score : scores) {
            maxScore = Math.max(maxScore, score);
        }
        return maxScore;
    }

    public void setScores(List<Double> scores) {
        this.scores = scores;
        invalidate();
        this.repaint();
    }

    public List<Double> getScores() {
        return scores;
    }

    private static List<String> guildMembers(){

        List<String> heroNames = new ArrayList<>();

        for(Hero h : guild.getHeroes()){
            heroNames.add(h.getHeroName());
        }
        return heroNames;
    }
}