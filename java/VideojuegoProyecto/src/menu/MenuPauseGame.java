/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package menu;

import characters.Jugador;
import imagen.Sprite;
import java.util.logging.Level;
import java.util.logging.Logger;
import location.Punto;
import org.newdawn.slick.gui.*;
import org.newdawn.slick.*;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.*;

/**
 *
 * @author Senapi Aroal
 */
public class MenuPauseGame implements ComponentListener{
    
    private Sprite fondo,continuar,controles,salir,imgControl,atras;
    private MouseOverArea[] botones = new MouseOverArea[4];
    private boolean pausa,debug,control;
    private int estado = -1;
    private GameContainer container;

    public MenuPauseGame(GameContainer container) {
        try{
            this.pausa = false;
            this.debug = false;
            this.control = false;
            this.container = container;
            this.fondo = new Sprite("./res/grafico/fonds/menu_pausa_nobotones_2.png");
            this.continuar = new Sprite("./res/grafico/buttons/boton_continuar.png",new Punto(250,250));
            this.controles = new Sprite("./res/grafico/buttons/boton_controles.png",new Punto(250,350));
            this.salir = new Sprite("./res/grafico/buttons/boton_salir.png",new Punto(378,500));
            this.atras = new Sprite("./res/grafico/buttons/boton_atras.png",new Punto(378,550));
            this.imgControl = new Sprite("./res/grafico/game_utils/controles.png");
            Sprite[] buttons = {continuar,controles,salir,atras};
            for(int i = 0;i<botones.length;i++){
                botones[i] = new MouseOverArea(container,buttons[i],(int)buttons[i].getPosicion().getX(),(int)buttons[i].getPosicion().getY(),buttons[i].getWidth(),buttons[i].getHeight(),this);         
                botones[i].setNormalColor(new Color(1,1,1,0.7f));
                botones[i].setMouseOverColor(new Color(1,1,1,0.9f));
            }
        }catch(SlickException ex){}
    }
    
    public void comprobarMenuPausa(Input entrada){
        if(entrada.isKeyPressed(Input.KEY_ESCAPE) && !control){
            if(pausa){
                pausa = false;
            }else{
                pausa = true;
            }
        }
        if(entrada.isKeyPressed(Input.KEY_F3) && !control){
            if(debug){
                debug = false;
            }else{
                debug = true;
            }
        }
    }
    
    public void comprobarEstado(GameContainer container,StateBasedGame game) throws SlickException{
        switch (estado) {
            case 0:
                //Continuar
                estado = -1;
                pausa = false;
                break;
            case 1:
                //Controles
                estado = -1;
                pausa = false;
                control = true;
                break;
            case 2:
                //Salir
                estado = -1;                  
                try {
                    game.enterState(0,FadeOutTransition.class.newInstance(), FadeInTransition.class.newInstance());
                } catch (InstantiationException ex) {
                    Logger.getLogger(MenuPauseGame.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(MenuPauseGame.class.getName()).log(Level.SEVERE, null, ex); 
                }
                break;
            case 3:
                //Atras
                estado = -1;
                control = false;
                pausa = true;
                break;
            default:
                break;
        }
    }
    
    public void gestionarMenuPausa(Input entrada,GameContainer container,StateBasedGame game) throws SlickException{
        this.comprobarMenuPausa(entrada);
        this.comprobarEstado(container, game);
    }
    
    public void mostrarMenu(Graphics g,Jugador j){
        if(debug){
            g.drawString("Eje x: " + String.format("%.3f",j.getPunto().getX()) + "  Eje y: " + String.format("%.3f",j.getPunto().getY()),20,20);
            g.drawString("Escenario " + j.getEscenario(),20,40);
            g.drawString("Municion " + j.getVarita().getMunicion(),20,60);
            g.drawString("Vida del jugador: " + j.getHp(),20,80);
            g.drawString("Velocidad del jugador: " + j.getVelocidad(),20 ,100 );
            g.drawString("Fuerza del jugador: " + j.getDanyo(),20 ,120 );
            g.drawString("Exp: " + j.getExperiencia() ,20,140);
            if(j.getBuffInv().getEstadoBuff()){
                g.drawString("Buff Invulnerabilidad: " + ((j.getBuffInv().getMaxTimeBuff()/1000) - (j.getBuffInv().getTimerEstadoBuff()/1000) ) + " seg" ,20,160);
            }
            if(j.getBuffFuerza().getEstadoBuff()){
                g.drawString("Buff Fuerza: " + ((j.getBuffFuerza().getMaxTimeBuff()/1000) - (j.getBuffFuerza().getTimerEstadoBuff()/1000) ) + " seg" ,20,180);
            }
            if(j.getBuffVelo().getEstadoBuff()){
                g.drawString("Buff Velocidad: " + ((j.getBuffVelo().getMaxTimeBuff()/1000) - (j.getBuffVelo().getTimerEstadoBuff()/1000) ) + " seg" ,20,200);
            }
        }
        if(pausa){
            fondo.draw();
            for(int i = 0;i<(botones.length-1);i++){
                botones[i].render(container, g);
            }
        }
        if(control){
            imgControl.draw();
            botones[3].render(container, g);
        }
    }
    
    public boolean isPausa() {
        return pausa;
    }
    
    @Override
    /**
     * @see org.newdawn.slick.gui.ComponentListener#componentActivated(org.newdawn.slick.gui.AbstractComponent)
     */
    public void componentActivated(AbstractComponent source) {
        for(int i = 0;i<botones.length;i++){
            if (source == botones[i]) {
                estado = i;
            }
        }
    }
    
}