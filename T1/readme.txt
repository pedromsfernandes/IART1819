Compilar

Não é necessário compilar o programa.

Executar

Pré-requisitos:
tkinter: apt-get install python3-tk

Em linux, abril um terminal dentro da pasta src/ e correr: python3 bloxorz.py
Em windows, correr: python blorxorz.py
De notar que tivemos algumas DificulDaDes a correr em windows, se bem que testamos nos PCs
da FEUP e correram corretamente.

Utilizar

Após abrir o programa, escolher o nível desejado usando os botões 'next' ou 'previous', e carregar
em 'play'.
Para relembrar o significado de cada cor, carregar no botão 'legend'.

Dentro da janela do jogo, usar as teclas WASD para mover, respetivamente, para cima, esquerda, baixo e direita. Note-se
que é necessário, antes de mover o bloco, carregar com o rato esquerdo na zona do tabuleiro para criar foco.

O utilizador pode então mover o bloco azul e direcioná-lo para a casa vermelha (objetivo), sendo que os resultados de cada 
movimento vão atualizando o tabuleiro. Se não acontecernada, quer dizer que o movimento não é válido.

O utilizador pode ainda resolver o nível com recurso aos algoritmos de pesquisa. Para isso usa
os botões 'previous' e 'next' para escolher o algoritmo desejado, e pode pedir uma dica para o próximo movimento
a tomar, ou pedir que o computador resolva o nível, sendo que nesse caso é apresentada uma animação com os sucessivos
movimentos a tomar. No final, são mostradas estatísticas, nomeadamente número de movimentos,
nós expandidos e tempo de resolução.
