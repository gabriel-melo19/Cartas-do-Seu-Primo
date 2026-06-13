## Card Game - JavaFX Edition

---

<img src="src/main/resources/com/cardgame/img/Logo.png" width="800" height="800">


Um jogo de cartas estratégico offline (Player vs Bot) desenvolvido em Java utilizando JavaFX para a interface gráfica. O projeto é focado na aplicação prática dos fundamentos da Programação Orientada a Objetos (POO), incluindo herança, polimorfismo, encapsulamento, abstração e design patterns como Factory e Strategy.

Utiliza o ecossistema Maven para gerenciamento de dependências e Jackson para persistência de dados localmente (sem banco de dados SQL).



---



## 📋 Funcionalidades Principais

### Mecânicas

- **Duelos Offline**: Combate contra Bots com decks personalizados e comportamentos distintos.
- **Sistema de Turnos**: Compra de carta por turno, troca dinâmica no tabuleiro 1x2 e ataque automático ao finalizar o turno.
- **Mecânica de Elementos**: Sistema de vantagens e desvantagens entre elementos (ex: Fogo vs Gelo) que modifica atributos temporariamente durante o duelo.
- **Vitória**: O jogador vence ao eliminar todas as cartas do oponente; não há vida direta para o personagem/jogador.

## 📋 Estrutura do Projeto


```
├── dependency-reduced-pom.xml
├── docs
│   ├── diagramas
│   │   └── diagrama_classes (substitua).png
│   └── manual
│       └── regras-jogo.md
├── .gitignore
├── .mvn
│   └── wrapper
│       ├── maven-wrapper.jar
│       └── maven-wrapper.properties
├── mvnw.cmd
├── pom.xml
├── Readme.md
├── saves
└── src
    └── main
        ├── java
        │   ├── com
        │   │   └── cardgame
        │   │       ├── effects
        │   │       │   ├── EfeitoCarta.java
        │   │       │   ├── EfeitoEscudoInicial.java
        │   │       │   └── EfeitoFactory.java
        │   │       ├── Launcher.java
        │   │       ├── logic
        │   │       │   ├── DueloResultado.java
        │   │       │   ├── MontadorDeck.java
        │   │       │   ├── PreparadorBatalha.java
        │   │       │   ├── SaveData.java
        │   │       │   ├── SessaoJogo.java
        │   │       │   └── SistemaCombate.java
        │   │       ├── Main.java
        │   │       ├── model
        │   │       │   ├── Bot.java
        │   │       │   ├── Carta.java
        │   │       │   ├── Deck.java
        │   │       │   ├── Elemento.java
        │   │       │   ├── JogadaResultado.java
        │   │       │   ├── JogadorHumano.java
        │   │       │   ├── Jogador.java
        │   │       │   └── TipoEfeito.java
        │   │       ├── persistence
        │   │       │   ├── RepositorioBots.java
        │   │       │   ├── RepositorioJSON.java
        │   │       │   └── RepositorioSave.java
        │   │       └── ui
        │   │           ├── AudioManager.java
        │   │           ├── CardNode.java
        │   │           ├── ControladorDeFluxo.java
        │   │           ├── controller
        │   │           │   ├── CarregarSaveController.java
        │   │           │   ├── JanelaBatalhaController.java
        │   │           │   ├── MenuPrincipalController.java
        │   │           │   ├── NicknameController.java
        │   │           │   ├── SelecaoAdversarioController.java
        │   │           │   └── SelecaoDeckController.java
        │   │           └── ScreenManager.java
        │   └── module-info.java
        └── resources
            └── com
                └── cardgame
                    ├── audio
                    │   ├── fundo_menu.wav
                    │   └── som_select.wav
                    ├── css
                    │   ├── estilo_adversario.css
                    │   ├── estilo_batalha.css
                    │   ├── estilo_deck.css
                    │   └── estilo_menu.css
                    ├── dados
                    │   ├── bots.json
                    │   └── cartas.json
                    ├── fxml
                    │   ├── estilo_menu.fxml
                    │   ├── janela_batalha.fxml
                    │   ├── menu_carregar_save.fxml
                    │   ├── menu_principal.fxml
                    │   ├── nickname.fxml
                    │   ├── selecao_adversario.fxml
                    │   └── selecao_deck.fxml
                    └── img
                        ├── cartas
                        │   ├── a_cota.png
                        │   ├── arlindo.png
                        │   ├── arvore_de_ferro.png
                        │   ├── a_senha.png
                        │   ├── avenged_sevenfold.png
                        │   ├── baba_is_you.png
                        │   ├── balatro.png
                        │   ├── cafe_bao.png
                        │   ├── chama_da_raposa.png
                        │   ├── charles_kuck.png
                        │   ├── cojack.png
                        │   ├── congalala.png
                        │   ├── creep_da_nevoa.png
                        │   ├── creeper.png
                        │   ├── empregado_do_mes.png
                        │   ├── eneas_carneiro.png
                        │   ├── exodia.png
                        │   ├── filosofo.png
                        │   ├── gaiola_de_faraday.png
                        │   ├── gotas_de_metal.png
                        │   ├── igeb.png
                        │   ├── inimigo_espelhado.png
                        │   ├── invasor_de_redes.png
                        │   ├── kiko_guitar_hero.png
                        │   ├── la_mano_de_dios.png
                        │   ├── life_is_but_a_dream.png
                        │   ├── luz_azul.png
                        │   ├── moon_dog.png
                        │   ├── odio.png
                        │   ├── olheca.png
                        │   ├── PC.png
                        │   ├── rick_astrey.png
                        │   ├── sao_paulo.png
                        │   ├── sapo_elegante.png
                        │   ├── sapo_trevoso.png
                        │   ├── sonic.png
                        │   ├── tenacious_d.png
                        │   ├── tiba.png
                        │   ├── toco.png
                        │   ├── tronco_que_danca.png
                        │   ├── trumpet_boy.png
                        │   ├── tudo_pelo_aluguel.png
                        │   ├── uma_lei.png
                        │   ├── validation.png
                        │   ├── vinhas.png
                        │   ├── violino_de_claudios.png
                        │   ├── voce.png
                        │   ├── washii.png
                        │   └── yung-lixo.png
                        ├── fundos
                        │   └── fundo_menu.jpg
                        ├── inimigos
                        │   ├── batman.png
                        │   ├── chapolin.png
                        │   ├── deadpool.png
                        │   ├── erick_jackan.png
                        │   ├── goku.png
                        │   ├── johnwick.png
                        │   ├── kiko.png
                        │   ├── leon_s_kennedy.png
                        │   ├── negonei.png
                        │   ├── seu_madruga.png
                        │   └── vegeta.png
                        └── Logo.png
```

---

## 🤝 Colaboradores

- **Gabriel Melo**
- **Marcos William**
- **Wesley Cardozo**

