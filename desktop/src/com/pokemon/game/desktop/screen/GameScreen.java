package com.pokemon.game.desktop.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.pokemon.game.desktop.Iniciar;
import com.pokemon.game.desktop.Settings;
import com.pokemon.game.desktop.controller.PlayerController;
import com.pokemon.game.desktop.model.Actor;
import com.pokemon.game.desktop.model.BasePokemon;
import com.pokemon.game.desktop.model.Camera;
import com.pokemon.game.desktop.model.CentroPokemon;
import com.pokemon.game.desktop.model.LojaPokemon;
import com.pokemon.game.desktop.model.PONTOSACAO;
import com.pokemon.game.desktop.model.Pokemon;
import com.pokemon.game.desktop.model.Pontuacao;
import com.pokemon.game.desktop.model.TERRAIN;
import com.pokemon.game.desktop.model.Tile;
import com.pokemon.game.desktop.model.TileMap;
import com.pokemon.game.desktop.model.Treinador;
import com.pokemon.game.desktop.util.AnimationSet;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.jpl7.Query;
import org.jpl7.Term;

public class GameScreen extends AbstractScreen {

    private PlayerController controller;
    private Camera camera;
    private Actor player;
    private Pontuacao pontuacao;
    private Treinador treinador;
    private CentroPokemon centropokemon;
    private LojaPokemon lojaPokemon;
    private Pokemon pokemon;
    private TileMap map;
    private SpriteBatch batch;
    private Texture redStandingSouth;
    private Texture grass1;
    private Texture grass2;
    private Texture montanha;
    private Texture caverna;
    private Texture agua;
    private Texture vulcao;
    private Texture backgroud_image;
    private Label textLabel;
    public int[][] terrenos = new int[42][42];
    private Texture render;
    private Texture sprite_treinador;
    private Texture backgrounDisplay;
    private int em_usoX;
    private int em_usoY;
    private int[][] posicoes_usadasPC = new int[42][42];
    private int[][] posicoes_usadasPM = new int[42][42];
    private int[][] posicoes_ocupadas_pokemons = new int[42][42];
    private int[][] posicoes_ocupadas_PM = new int[42][42];
    private int[][] posicoes_ocupadas_PC = new int[42][42];
    private int[][] posicoes_ocupadas_Treinador = new int[42][42];

    private int direcao_mapa_x;
    private int direcao_mapa_y;
    private List<Treinador> sprites_treinador = new ArrayList<Treinador>();
    private List<CentroPokemon> sprites_pokecenter = new ArrayList<CentroPokemon>();
    private List<LojaPokemon> sprites_pokemart = new ArrayList<LojaPokemon>();
    private List<Pokemon> sprites_pokemons = new ArrayList<Pokemon>();
    private List<String> nomes_pokemons = new ArrayList<String>();

    List<Integer> list = new ArrayList<Integer>();
    private TextField txtDisplay;

    private int score;
    private int score2;
    private String pontuacao_atual;
    private String pontuacao_total;
    BitmapFont fonte;
    BitmapFont fonte2;
    Group grp = new Group();
    private Stage stage;
    private Image img;

    /*Constantes para indicar que os valores numéricos no array: valores_arquivo 
    correspondem aos nomes dos 5 tipos de terrenos.
     */
    public static final int GRAMA = 1;
    public static final int MONTANHA = 2;
    public static final int CAVERNA = 3;
    public static final int AGUA = 4;
    public static final int VULCAO = 5;

    private BitmapFont font;
    private TextureAtlas buttonsAtlas; //** image of buttons **//
    private Skin buttonSkin; //** images are used as skins of the button **//
    private TextButton button;
    private TextButtonStyle textButtonStyle;
    private BasePokemon agente = new BasePokemon(); 
    
    
    /*private String nome_arquivo;
    private Query compilar_arquivo;
    private String regra;
    private Query executar_regra;*/
    private String direcao;
    //private Map<String, Term> resultado_regra;
    
    public GameScreen(Iniciar app) {
        super(app);
        redStandingSouth = new Texture("ash_south_stand.png");
        grass1 = new Texture("grass1.png");
        grass2 = new Texture("grass2.png");
        montanha = new Texture("mountain.png");
        caverna = new Texture("cave.png");
        agua = new Texture("water.png");
        vulcao = new Texture("volcano.png");

        batch = new SpriteBatch();

        stage = new Stage(new StretchViewport(800, 800));
        font = new BitmapFont(Gdx.files.internal("fontes/small_letters_font.fnt"),
                Gdx.files.internal("fontes/small_letters_font.png"), false);

        TextureAtlas atlas = app.getAssetManager().get("packed/textures.atlas", TextureAtlas.class);

        AnimationSet animations = new AnimationSet(
                new Animation(0.3f / 2f, atlas.findRegions("ash_north_walk"), PlayMode.LOOP_PINGPONG),
                new Animation(0.3f / 2f, atlas.findRegions("ash_south_walk"), PlayMode.LOOP_PINGPONG),
                new Animation(0.3f / 2f, atlas.findRegions("ash_east_walk"), PlayMode.LOOP_PINGPONG),
                new Animation(0.3f / 2f, atlas.findRegions("ash_west_walk"), PlayMode.LOOP_PINGPONG),
                atlas.findRegion("ash_north_stand"),
                atlas.findRegion("ash_south_stand"),
                atlas.findRegion("ash_east_stand"),
                atlas.findRegion("ash_west_stand")
        );

        map = new TileMap(42, 42);
        player = new Actor(map, 24, 22, animations);
        pontuacao = new Pontuacao(0);

        //Gera as instâncias dos centros pokemon
        gerar_centros_pokemon();

        //Gera as instâncias das Lojas Pokemon   
        gerar_lojas_pokemon();

        //Define a lista de nomes de pokemons e seus tipos
        adicionar_nomes_pokemon();

        //Gera Pokémons aleatórios e guarda as instâncias deles
        gerar_pokemons();

        //Gera treinadores aleatórios e guarda as instâncias deles   
        gerar_Treinador();
        
        //Atualizar a pontuacao com base nas ações 
        //Recebe objeto Pontuacao para mostrar o valor da Pontuacao
        pontuacao.ganharBatalha();
        pontuacao.recuprarPokemons();
        mostrar_Pontuacao_Atual(pontuacao);
        mostrar_Pontuacao_Total(pontuacao);

        //Mostrar Pokémons Capturados!
        pontuacao.setPokemons_capturados(15);
        mostrar_Qtd_Pokemons(pontuacao);

        camera = new Camera();

        controller = new PlayerController(player);
    }
    
    public void mostrar_Qtd_Pokemons(Pontuacao pontuacao_poke) {

        font.getData().setScale(1.5f);
        buttonSkin = new Skin();
        buttonsAtlas = new TextureAtlas(Gdx.files.internal("packed2/uipack.atlas"));
        buttonSkin.addRegions(buttonsAtlas);
        textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = buttonSkin.getDrawable("dialoguebox");
        button = new TextButton("Capturados " + pontuacao_poke.getPokemons_capturados(), textButtonStyle);
        button.setPosition(612, 500);
        stage.addActor(button);

    }

    public void mostrar_Pontuacao_Atual(Pontuacao pontuacao_atual) {

        font.getData().setScale(1.5f);
        buttonSkin = new Skin();
        buttonsAtlas = new TextureAtlas(Gdx.files.internal("packed2/uipack.atlas"));
        buttonSkin.addRegions(buttonsAtlas);
        textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = buttonSkin.getDrawable("dialoguebox");
        button = new TextButton("Pontuacao Atual " + pontuacao_atual.getPontuacaoAtual(), textButtonStyle);
        button.setPosition(612, 700);
        stage.addActor(button);

    }

    public void mostrar_Pontuacao_Total(Pontuacao valor_Pontuacao) {
        font.getData().setScale(1.5f);
        buttonSkin = new Skin();
        buttonsAtlas = new TextureAtlas(Gdx.files.internal("packed2/uipack.atlas"));
        buttonSkin.addRegions(buttonsAtlas);
        textButtonStyle = new TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.up = buttonSkin.getDrawable("dialoguebox");
        button = new TextButton("Pontuacao Total " + valor_Pontuacao.getPontuacaoTotal(), textButtonStyle);
        button.setPosition(612, 600);
        stage.addActor(button);

    }

    public void gerar_Treinador() {
        for (int x = 0; x < 50; x++) {
            int direcao_face = (int) (Math.random() * 4 + 1);
            int tipo_treinador = (int) (Math.random() * 6 + 1);
            direcao_mapa_x = (int) (Math.random() * 41 + 0);
            direcao_mapa_y = (int) (Math.random() * 41 + 0);

            if (posicoes_usadasPC[direcao_mapa_x][direcao_mapa_y] != 1
                    || posicoes_usadasPM[direcao_mapa_x][direcao_mapa_y] != 1
                    || posicoes_ocupadas_pokemons[direcao_mapa_x][direcao_mapa_y] != 1) {

                if (x == 0) {
                    posicoes_ocupadas_Treinador[direcao_mapa_x][direcao_mapa_y] = 1;
                }

                if (x > 0) {
                    while (!getposicao_treinador_ocupada(direcao_mapa_x, direcao_mapa_y)
                            || !getposicao_pokemon_ocupada(direcao_mapa_x, direcao_mapa_y)
                            || !getposicao_PC_ocupada(direcao_mapa_x, direcao_mapa_y)
                            || !getposicao_PM_ocupada(direcao_mapa_x, direcao_mapa_y)) {
                        direcao_mapa_x = (int) (Math.random() * 41 + 0);
                        direcao_mapa_y = (int) (Math.random() * 41 + 0);
                    }
                    posicoes_ocupadas_Treinador[direcao_mapa_x][direcao_mapa_y] = 1;
                }

                if (getposicao_treinador_ocupada(direcao_mapa_x, direcao_mapa_y)
                            || getposicao_pokemon_ocupada(direcao_mapa_x, direcao_mapa_y)
                            || getposicao_PC_ocupada(direcao_mapa_x, direcao_mapa_y)
                            || getposicao_PM_ocupada(direcao_mapa_x, direcao_mapa_y)) 
                {
                    treinador = new Treinador(map, 22, 22, direcao_face, tipo_treinador);
                    sprites_treinador.add(treinador);
                } else {
                    posicoes_ocupadas_Treinador[direcao_mapa_x][direcao_mapa_y] = 0;
                    x--;
                }
            } else {
                x--;
            }
        }
    }

    public void gerar_pokemons() {
        for (int x = 0; x < 150; x++) {
            direcao_mapa_x = (int) (Math.random() * 41 + 0);
            direcao_mapa_y = (int) (Math.random() * 41 + 0);

            if (posicoes_usadasPC[direcao_mapa_x][direcao_mapa_y] != 1
                    || posicoes_usadasPM[direcao_mapa_x][direcao_mapa_y] != 1) {

                if (x == 0) {
                    posicoes_ocupadas_pokemons[direcao_mapa_x][direcao_mapa_y] = 1;
                }

                if (x > 0) {
                    while (!getposicao_pokemon_ocupada(direcao_mapa_x, direcao_mapa_y)
                            || !getposicao_PC_ocupada(direcao_mapa_x, direcao_mapa_y)
                            || !getposicao_PM_ocupada(direcao_mapa_x, direcao_mapa_y)) {
                        direcao_mapa_x = (int) (Math.random() * 41 + 0);
                        direcao_mapa_y = (int) (Math.random() * 41 + 0);
                    }
                    posicoes_ocupadas_pokemons[direcao_mapa_x][direcao_mapa_y] = 1;
                }

                if (getposicao_treinador_ocupada(direcao_mapa_x, direcao_mapa_y) ||
                        getposicao_pokemon_ocupada(direcao_mapa_x, direcao_mapa_y)
                            || getposicao_PC_ocupada(direcao_mapa_x, direcao_mapa_y)
                            || getposicao_PM_ocupada(direcao_mapa_x, direcao_mapa_y))                              
                {
                    /*if(nomes_pokemons.get(x).equals("pidgey-normal;voador")){
                        direcao_mapa_x = 23;
                        direcao_mapa_y = 22;
                    }*/
                    pokemon = new Pokemon(map, direcao_mapa_x, direcao_mapa_y,
                            nomes_pokemons.get(x));
                    sprites_pokemons.add(pokemon);
                } else {
                    posicoes_ocupadas_pokemons[direcao_mapa_x][direcao_mapa_y] = 0;
                    x--;
                }
            } else {
                x--;
            }
        }
    }

    public void gerar_lojas_pokemon() {

        for (int x = 0; x < 15; x++) {
            direcao_mapa_x = (int) (Math.random() * 41 + 0);
            direcao_mapa_y = (int) (Math.random() * 41 + 0);

            if (posicoes_usadasPC[direcao_mapa_x][direcao_mapa_y] != 1) {
                if (x == 0) {
                    posicoes_ocupadas_PM[direcao_mapa_x][direcao_mapa_y] = 1;
                }

                if (x > 0) {
                    while (!getposicao_PC_ocupada(direcao_mapa_x, direcao_mapa_y)
                            || !getposicao_PM_ocupada(direcao_mapa_x, direcao_mapa_y)) {
                        direcao_mapa_x = (int) (Math.random() * 41 + 0);
                        direcao_mapa_y = (int) (Math.random() * 41 + 0);
                    }
                    posicoes_ocupadas_PM[direcao_mapa_x][direcao_mapa_y] = 1;
                }
                if (posicoes_usadasPC[direcao_mapa_x][direcao_mapa_y] != 1) {
                    lojaPokemon = new LojaPokemon(map, direcao_mapa_x, direcao_mapa_y);
                    sprites_pokemart.add(lojaPokemon);
                } else {
                    posicoes_ocupadas_PM[direcao_mapa_x][direcao_mapa_y] = 0;
                    x--;
                }
            } else {
                x--;
            }
        }
    }

    public void gerar_centros_pokemon() {

        for (int x = 0; x < 20; x++) {
            direcao_mapa_x = (int) (Math.random() * 41 + 0);
            direcao_mapa_y = (int) (Math.random() * 41 + 0);

            if (x == 0) {
                posicoes_ocupadas_PC[direcao_mapa_x][direcao_mapa_y] = 1;
            }

            if (x > 0) {
                while (!getposicao_treinador_ocupada(direcao_mapa_x, direcao_mapa_y)
                        || !getposicao_pokemon_ocupada(direcao_mapa_x, direcao_mapa_y)
                        || !getposicao_PC_ocupada(direcao_mapa_x, direcao_mapa_y)
                        || !getposicao_PM_ocupada(direcao_mapa_x, direcao_mapa_y)) {
                    direcao_mapa_x = (int) (Math.random() * 41 + 0);
                    direcao_mapa_y = (int) (Math.random() * 41 + 0);
                }
                posicoes_ocupadas_PC[direcao_mapa_x][direcao_mapa_y] = 1;
            }

            centropokemon = new CentroPokemon(map, direcao_mapa_x, direcao_mapa_y);
            sprites_pokecenter.add(centropokemon);
        }
    }

    public boolean getposicao_PC_ocupada(int x, int y) {
        for (int l = 0; l < 42; l++) {
            for (int c = 0; c < 42; c++) {
                if (posicoes_ocupadas_PC[x][y] == 1) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean getposicao_PM_ocupada(int x, int y) {
        for (int l = 0; l < 42; l++) {
            for (int c = 0; c < 42; c++) {
                if (posicoes_ocupadas_PM[x][y] == 1) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean getposicao_pokemon_ocupada(int x, int y) {
        for (int l = 0; l < 42; l++) {
            for (int c = 0; c < 42; c++) {
                if (posicoes_ocupadas_pokemons[x][y] == 1) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean getposicao_treinador_ocupada(int x, int y) {
        for (int l = 0; l < 42; l++) {
            for (int c = 0; c < 42; c++) {
                if (posicoes_ocupadas_Treinador[x][y] == 1) {
                    return false;
                }
            }
        }

        return true;
    }

    public void adicionar_nomes_pokemon() {
        nomes_pokemons.add("abra-psiquico;N");
        nomes_pokemons.add("alakazam-psiquico;N");
        nomes_pokemons.add("aerodactyl-pedra;voador");
        nomes_pokemons.add("arbok-venenoso;N");
        nomes_pokemons.add("arcanine-fogo;N");
        nomes_pokemons.add("articuno-gelo;voador");
        nomes_pokemons.add("beedrill-inseto;voador");
        nomes_pokemons.add("bellsprout-grama;venenoso");
        nomes_pokemons.add("blastoise-agua;N");
        nomes_pokemons.add("bulbasaur-grama;venenoso");
        nomes_pokemons.add("butterfree-inseto;voador");
        nomes_pokemons.add("caterpie-inseto;N");
        nomes_pokemons.add("chansey-normal;N");
        nomes_pokemons.add("charizard-fogo;voador");
        nomes_pokemons.add("charmander-fogo;N");
        nomes_pokemons.add("charmeleon-fogo;N");
        nomes_pokemons.add("clefable-fada;N");
        nomes_pokemons.add("clefairy-fada;N");
        nomes_pokemons.add("cloyster-agua;gelo");
        nomes_pokemons.add("cubone-terra;N");
        nomes_pokemons.add("dewgong-agua;gelo");
        nomes_pokemons.add("diglett-terra;N");
        nomes_pokemons.add("ditto-normal;N");
        nomes_pokemons.add("dodrio-normal;voador");
        nomes_pokemons.add("doduo-normal;voador");
        nomes_pokemons.add("dragonair-dragao;N");
        nomes_pokemons.add("dragonite-dragao;voador");
        nomes_pokemons.add("dratini-dragao;N");
        nomes_pokemons.add("drowzee-psiquico;N");
        nomes_pokemons.add("dugtrio-terra;N");
        nomes_pokemons.add("eevee-normal;N");
        nomes_pokemons.add("ekans-venenoso;N");
        nomes_pokemons.add("electabuzz-eletrico;N");
        nomes_pokemons.add("electrode-eletrico;N");
        nomes_pokemons.add("exeggcute-grama;psiquico");
        nomes_pokemons.add("exeggutor-grama;psiquico");
        nomes_pokemons.add("farfetchd-normal;voador");
        nomes_pokemons.add("fearow-normal;voador");
        nomes_pokemons.add("flareon-fogo;N");
        nomes_pokemons.add("gastly-fantasma;venenoso");
        nomes_pokemons.add("gengar-fantasma;venenoso");
        nomes_pokemons.add("geodude-pedra;terra");
        nomes_pokemons.add("gloom-grama;venenoso");
        nomes_pokemons.add("golbat-venenoso;voador");
        nomes_pokemons.add("goldeen-agua;N");
        nomes_pokemons.add("golduck-agua;N");
        nomes_pokemons.add("golem-pedra;terra");
        nomes_pokemons.add("graveler-pedra;terra");
        nomes_pokemons.add("grimer-venenoso;N");
        nomes_pokemons.add("growlithe-fogo;N");
        nomes_pokemons.add("gyarados-agua;voador");
        nomes_pokemons.add("haunter-fantasma;venenoso");
        nomes_pokemons.add("hitmonchan-lutador;N");
        nomes_pokemons.add("hitmonlee-lutador;N");
        nomes_pokemons.add("horsea-agua;N");
        nomes_pokemons.add("hypno-psiquico;N");
        nomes_pokemons.add("ivysaur-grama;venenoso");
        nomes_pokemons.add("jigglypuff-normal;fada");
        nomes_pokemons.add("jolteon-eletrico;N");
        nomes_pokemons.add("jynx-gelo;psiquico");
        nomes_pokemons.add("kabuto-pedra;agua");
        nomes_pokemons.add("kabutops-pedra;agua");
        nomes_pokemons.add("kadabra-psiquico;N");
        nomes_pokemons.add("kakuna-inseto;venenoso");
        nomes_pokemons.add("kangaskhan-normal;N");
        nomes_pokemons.add("kingler-agua;N");
        nomes_pokemons.add("koffing-venenoso;N");
        nomes_pokemons.add("krabby-agua;N");
        nomes_pokemons.add("lapras-agua;gelo");
        nomes_pokemons.add("lickitung-normal;N");
        nomes_pokemons.add("machamp-lutador;N");
        nomes_pokemons.add("machoke-lutador;N");
        nomes_pokemons.add("machop-lutador;N");
        nomes_pokemons.add("magikarp-agua;N");
        nomes_pokemons.add("magmar-fogo;N");
        nomes_pokemons.add("magnemite-eletrico;aco");
        nomes_pokemons.add("magneton-eletrico;aco");
        nomes_pokemons.add("mankey-lutador;N");
        nomes_pokemons.add("marowak-terra;N");
        nomes_pokemons.add("meowth-normal;N");
        nomes_pokemons.add("metapod-inseto;N");
        nomes_pokemons.add("mewtwo-psiquico;N");
        nomes_pokemons.add("moltres-fogo;voador");
        nomes_pokemons.add("mrMime-psiquico;fada");
        nomes_pokemons.add("muk-venenoso;N");
        nomes_pokemons.add("nidoking-venenoso;terra");
        nomes_pokemons.add("nidoqueen-venenoso;terra");
        nomes_pokemons.add("nidoranf-venenoso;N");
        nomes_pokemons.add("nidoranm-venenoso;N");
        nomes_pokemons.add("nidorina-venenoso;N");
        nomes_pokemons.add("nidorino-venenoso;N");
        nomes_pokemons.add("ninetales-fogo;N");
        nomes_pokemons.add("oddish-grama;venenoso");
        nomes_pokemons.add("omanyte-pedra;agua");
        nomes_pokemons.add("omastar-pedra;agua");
        nomes_pokemons.add("onix-pedra;terra");
        nomes_pokemons.add("paras-inseto;grama");
        nomes_pokemons.add("parasect-inseto;grama");
        nomes_pokemons.add("persian-normal;N");
        nomes_pokemons.add("pidgeot-normal;voador");
        nomes_pokemons.add("pidgeotto-normal;voador");
        nomes_pokemons.add("pidgey-normal;voador");
        nomes_pokemons.add("pikachu-eletrico;N");
        nomes_pokemons.add("pinsir-inseto;N");
        nomes_pokemons.add("poliwag-agua;N");
        nomes_pokemons.add("poliwhirl-agua;N");
        nomes_pokemons.add("poliwrath-agua;lutador");
        nomes_pokemons.add("ponyta-fogo;N");
        nomes_pokemons.add("porygon-normal;N");
        nomes_pokemons.add("primeape-lutador;N");
        nomes_pokemons.add("psyduck-agua;N");
        nomes_pokemons.add("raichu-eletrico;N");
        nomes_pokemons.add("rapidash-fogo;N");
        nomes_pokemons.add("raticate-normal;N");
        nomes_pokemons.add("rattata-normal;N");
        nomes_pokemons.add("rhydon-terra;pedra");
        nomes_pokemons.add("rhyhorn-terra;pedra");
        nomes_pokemons.add("sandshrew-terra;N");
        nomes_pokemons.add("sandslash-terra;N");
        nomes_pokemons.add("scyther-inseto;voador");
        nomes_pokemons.add("seadra-agua;N");
        nomes_pokemons.add("seaking-agua;N");
        nomes_pokemons.add("seel-agua;N");
        nomes_pokemons.add("shellder-agua;N");
        nomes_pokemons.add("slowbro-agua;psiquico");
        nomes_pokemons.add("slowpoke-agua;psiquico");
        nomes_pokemons.add("snorlax-normal;N");
        nomes_pokemons.add("spearow-normal;voador");
        nomes_pokemons.add("squirtle-agua;N");
        nomes_pokemons.add("starmie-agua;psiquico");
        nomes_pokemons.add("staryu-agua;N");
        nomes_pokemons.add("tangela-grama;N");
        nomes_pokemons.add("tauros-normal;N");
        nomes_pokemons.add("tentacool-agua;venenoso");
        nomes_pokemons.add("tentacruel-agua;venenoso");
        nomes_pokemons.add("vaporeon-agua;N");
        nomes_pokemons.add("venomoth-inseto;venenoso");
        nomes_pokemons.add("venonat-inseto;venenoso");
        nomes_pokemons.add("venusaur-grama;venenoso");
        nomes_pokemons.add("victreebel-grama;venenoso");
        nomes_pokemons.add("vileplume-grama;venenoso");
        nomes_pokemons.add("voltorb-eletrico;N");
        nomes_pokemons.add("vulpix-fogo;N");
        nomes_pokemons.add("wartortle-agua;N");
        nomes_pokemons.add("weedle-inseto;venenoso");
        nomes_pokemons.add("weepinbell-grama;venenoso");
        nomes_pokemons.add("weezing-venenoso;N");
        nomes_pokemons.add("wigglytuff-normal;fada");
        nomes_pokemons.add("zapdos-eletrico;voador");
        nomes_pokemons.add("zubat-venenoso;voador");
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);

    }
    
    @Override
    public void render(float delta) {

        //(LEIA AQUI) Retorna o que tem na posição atual e nas adjacentes    
        System.out.println(map.getTerrenos(player.getX(), player.getY()).getTipo_Objeto());
        
        if(player.getY() < 41 && player.getX() < 41 && player.getY() > 0 && player.getX() > 0){
            System.out.println("Norte: " + map.getTerrenos(player.getX(), player.getY() + 1).getTipo_Objeto());
            System.out.println("Sul: " + map.getTerrenos(player.getX(), player.getY() -1).getTipo_Objeto());
            System.out.println("Leste: " + map.getTerrenos(player.getX() + 1, player.getY()).getTipo_Objeto());
            System.out.println("Oeste: " + map.getTerrenos(player.getX() - 1, player.getY()).getTipo_Objeto());
        }

        String objeto = map.getTerrenos(player.getX(), player.getY()).getTipo_Objeto();

        if (objeto.equals("treinador")) {
            System.out.println("######Chamada#####");
            if(agente.enfrentarTreinador(map.getTerrenos(player.getX(), player.getY()).getTreinador())){
                map.getTerrenos(player.getX(), player.getY()).getTreinador().setVisibilidade(false);
                agente.atualizarPokemonNaBase();
                agente.inserirTreinadorNaBase(map.getTerrenos(player.getX(), player.getY()).getTreinador());
                agente.imprimirResultado();
                agente.listarPokemonsNaBase();
            }
        } else if (objeto.equals("pokemon")) {
            if(!agente.verificarPokemonNaBase(map.getTerrenos(player.getX(), player.getY()).getPokemon())){
                map.getTerrenos(player.getX(), player.getY()).getPokemon().setVisibilidade(false);
                agente.inserirPokemonNaBase(map.getTerrenos(player.getX(), player.getY()).getPokemon());
                agente.listarPokemonsNaBase();
            }
        } else {
            objeto = "null";
        }
        
        direcao = agente.buscarNaBase(map, player);
        
        if(!direcao.equals("parado")){
            controller.update(delta, direcao);
            player.update(delta);
        }

        //Descomente para movimentar a câmera
        //camera.update(player.getWorldX() + 0.5f, player.getWorldY() + 0.5f);
        batch.begin();

        float worldStartX = Gdx.graphics.getWidth() / 14 - camera.getCameraX() * Settings.SCALED_TILE_SIZE;
        float worldStartY = Gdx.graphics.getHeight() / 14 - camera.getCameraY() * Settings.SCALED_TILE_SIZE;

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {

                switch (map.getTerrenos(x, y).getTerrain()) {
                    case GRASS_2:
                        render = grass2;
                        break;
                    case MONTANHA:
                        render = montanha;
                        break;
                    case CAVERNA:
                        render = caverna;
                        break;
                    case AGUA:
                        render = agua;
                        break;
                    case VULCAO:
                        render = vulcao;
                        break;
                    default:
                        break;
                }

                batch.draw(render,
                        worldStartX + x * Settings.SCALED_TILE_SIZE,
                        worldStartY + y * Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE);
            }

        }

        batch.draw(player.getSprite(),
                worldStartX + player.getWorldX() * Settings.SCALED_TILE_SIZE,
                worldStartY + player.getWorldY() * Settings.SCALED_TILE_SIZE,
                Settings.SCALED_TILE_SIZE,
                Settings.SCALED_TILE_SIZE * 1.5f);

        //Desenha os treinadores na tela 
        for (Treinador trainer : sprites_treinador) {
            if (trainer.isVisibilidade()) {
                batch.draw(trainer.getSprite(),
                        worldStartX + trainer.getWorldX() * Settings.SCALED_TILE_SIZE,
                        worldStartY + trainer.getWorldY() * Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE * 1.5f);
            } else {
                switch (map.getTerrenos(trainer.getX(), trainer.getY()).getTerrain()) 
                {
                    case GRASS_2:
                        render = grass2;
                        break;
                    case MONTANHA:
                        render = montanha;
                        break;
                    case CAVERNA:
                        render = caverna;
                        break;
                    case AGUA:
                        render = agua;
                        break;
                    case VULCAO:
                        render = vulcao;
                        break;
                    default:
                        break;                        
                }
                
                batch.draw(render,
                        worldStartX + trainer.getX() * Settings.SCALED_TILE_SIZE,
                        worldStartY + trainer.getY() * Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE);
            }
        }

        //Desenha os centros pokemon na tela 
        for (CentroPokemon pokecenter : sprites_pokecenter) {
            batch.draw(pokecenter.getSprite(),
                    worldStartX + pokecenter.getWorldX() * Settings.SCALED_TILE_SIZE,
                    worldStartY + pokecenter.getWorldY() * Settings.SCALED_TILE_SIZE,
                    Settings.SCALED_TILE_SIZE,
                    Settings.SCALED_TILE_SIZE * 1.5f
            );
        }

        //Desenha as Lojas Pokemon na tela 
        for (LojaPokemon pokemart : sprites_pokemart) {
            batch.draw(pokemart.getSprite(),
                    worldStartX + pokemart.getWorldX() * Settings.SCALED_TILE_SIZE,
                    worldStartY + pokemart.getWorldY() * Settings.SCALED_TILE_SIZE,
                    Settings.SCALED_TILE_SIZE,
                    Settings.SCALED_TILE_SIZE * 1.5f
            );
        }

        //Desenha os Pokemons na tela 
        for (Pokemon pokemon : sprites_pokemons) {
            
            if(pokemon.isVisibilidade()){
                batch.draw(pokemon.getSprite(),
                        worldStartX + pokemon.getWorldX() * Settings.SCALED_TILE_SIZE,
                        worldStartY + pokemon.getWorldY() * Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE * 1.5f
                );
            }else {
                switch (map.getTerrenos(pokemon.getX(), pokemon.getY()).getTerrain()) 
                {
                    case GRASS_2:
                        render = grass2;
                        break;
                    case MONTANHA:
                        render = montanha;
                        break;
                    case CAVERNA:
                        render = caverna;
                        break;
                    case AGUA:
                        render = agua;
                        break;
                    case VULCAO:
                        render = vulcao;
                        break;
                    default:
                        break;                        
                }
                
                batch.draw(render,
                        worldStartX + pokemon.getX() * Settings.SCALED_TILE_SIZE,
                        worldStartY + pokemon.getY() * Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE,
                        Settings.SCALED_TILE_SIZE);
            }
        }
        batch.end();

        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
    
}
