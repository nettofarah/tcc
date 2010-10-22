% Classe UFLAMON - Monografia de Graduação da UFLA
%
% Desenvolvida e mantido por Joaquim Quinteiro Uchôa (joukim@ginux.ufla.br),
% a partir do modelo da universidade.
%
% A primeira versão, em 2001, foi desenvolvida com o auxí­lio de 
% Jones Oliveira de Albuquerque. 
%
% A classe foi aperfeiçoada a partir de
% contribuições de usuários da mesma, entre eles:
% Mário Luiz Rodrigues
% Douglas Machado Tavares
% Samuel Pereira Dias
%
% Alterada em 25/11/2004 por Joaquim Q. Uchôa:
% -> remoção do uso da classe geometry para determinação das margens,
%     classe agora define margens usando comandos LaTeX puros :-)
% Alterada em 05/01/2005 por Joaquim Q. Uchôa:
% -> adoção do pdfLaTeX para resoluíoo de problemas com fontes
%    e hyperref
% -> melhor uso dos comandos de definição de margens
% Alterada em 12/09/2005 por Joaquim Q. Uchôa 
%  (com contribuições pontuais de Samuel P. Dias):
% -> melhoria do arquivo de exemplo, com uso de tabelas, figuras,
%    inserção de códigos, etc.
% Alterada em 03/07/2006 por Joaquim Q. Uchôa:
% -> inclusão da terceira banca
% Alterada em 11/09/2006 por Joaquim Q. Uchôa:
% -> migração para utf8 e inclusão de palavras-chave
% Alterada em 07/03/2008 por Joaquim Q. Uchôa:
% -> adequação da classe para novas normas da graduação
% Alterada em 14/06/2010 por Renato R. R. Oliveira:
% -> Modificacoes para atender as novas normas da UFLA.
% -> Codificado em latin1, porque utf-8 � porcaria =D


% identificacao
\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{uflamon}[2010/06/14]

% Compilacao preferencial em pdfLaTeX
\ifx\pdfoutput\undefined
\typeout{}
\typeout{*******************************************************************}
\typeout{* AVISO: Essa classe não foi feita para ser usada com LaTeX puro! }
\typeout{* Use pdfLaTeX!                                                   }
\typeout{*                                                                 }
\typeout{* Se você antes utilizava o comando                               }
\typeout{* latex arquivo; dvips arquivo                                    }
\typeout{* Então basta agora digitar                                       }
\typeout{* pdflatex arquivo                                                }
\typeout{*                                                                 }
\typeout{* Dicas:                                                          }
\typeout{* - Ao usar imagens vetoriais, prefira exportar para pdf direto   }
\typeout{*   ou então exporte para eps e utilize o comando                 }
\typeout{*   epstopdf arquivo.eps                                          }
\typeout{*   para gerar um pdf a partir de um eps                          }
\typeout{* - Ao usar imagens bitmaps, prefira o formato PNG, ou então      }
\typeout{*   JPEG                                                          }
\typeout{*******************************************************************}
\typeout{}
\stop
\fi

% código inicial e carregamento de pacotes
\LoadClass[11pt,letterpaper,oneside]{article} % classe base para a monografia
\RequirePackage{ifthen} % para testes
\RequirePackage{calc} % para cí¡lculos (margens, etc.)
\RequirePackage{lastpage} % para ficha catalográfica
\RequirePackage{graphicx} % Logo da UFLA na capa.
\RequirePackage{setspace} % Pacote para espacamento de linhas.
\RequirePackage{indentfirst}

% Definção de margens
\setlength{\hoffset}{-1in} % remove padrão do LaTeX 
\setlength{\voffset}{-1in} % remove padrão do LaTeX 
\setlength{\oddsidemargin}{4.5cm}  % margem esquerda
\setlength{\evensidemargin}{4.5cm} % margem direita
\setlength{\topmargin}{4.5cm}    % margem superior
\newlength{\bottommargin}        % margem inferior é deduzida em LaTeX
\setlength{\bottommargin}{4.5cm} % margem inferior
\setlength{\headheight}{0cm} % altura do cabeçalho
\setlength{\headsep}{0pt}    % espaço entre cabeçalho e texto
\setlength{\footskip}{0cm}  % espaço entre rodapé e texto
\setlength\textheight{ % altura disponí­vel para texto
 \paperheight-\topmargin-\bottommargin-\headheight-\headsep-\footskip
} 
\setlength\textwidth{  % largura disponí­vel para texto
 \paperwidth-\oddsidemargin-\evensidemargin
}

\renewcommand{\baselinestretch}{1.5}\normalsize %espaçamento

% cabeçalho e rodapé
\RequirePackage{fancyhdr}
\fancyhead{}
\fancyhead[RO]{\nouppercase{\small\slshape\rightmark}}
\fancyhead[LE]{\nouppercase{\small\slshape\leftmark}}
\fancyfoot{}
\fancyfoot[CO,CE]{\small\thepage}

\fancypagestyle{plain}{
\fancyhf{}
%\fancyfoot[C]{\thepage}
\renewcommand{\headrulewidth}{0pt}
\renewcommand{\footrulewidth}{0pt}
}

\fancypagestyle{ufla}{
\fancyhf{}
\fancyhead[RO,RE]{\thepage}
\renewcommand{\headrulewidth}{0pt}
\renewcommand{\footrulewidth}{0pt}
\setlength{\headsep}{1.5cm}
}


% Tipos de fontes
\RequirePackage{pslatex} %times, helvetica e courier
\RequirePackage{mathptmx} % fontes matemáticas em ps


% Controle de linhas orfãs e viúvas (desabilitar)
\widowpenalty10000 % 10000 significa virtualmente infinito. 
\clubpenalty10000


\newlanguage\nohyphen
\newcommand\nh[1]{\language\nohyphen #1}

% abstract e thanks
\renewcommand{\thanks}[1]{
                          \cleardoublepage
                          \null\vfill
                          \begin{center}\textbf{AGRADECIMENTOS}\end{center}
                          \begin{quote}#1\end{quote}
                          \vspace*{1cm}
                          \vfill\null
                          \clearpage
                         }

\def\palchaves#1{\gdef\@palchaves{\nh{#1}}} % palavras-chave da monografia
\palchaves{} %inicialmente vazio
\def\keywords#1{\gdef\@keywords{\nh{#1}}} % palavras-chave da monografia (em inglês)
\keywords{} %inicialmente vazio

\newcommand{\resumo}[1]{
\begin{center}\textbf{RESUMO}\end{center}
\begin{singlespace}

#1\vspace{22pt}\\
Palavras-chave: \@palchaves
\end{singlespace}
\newpage
}

\newcommand{\resumoingles}[1]{
\begin{center}\textbf{ABSTRACT}\end{center}
\begin{singlespace}

#1\vspace{22pt}\\
Keywords: \@keywords
\end{singlespace}
\newpage
}
\newcommand{\dedic}[1]{
                          \clearpage
                          \null\vfill
                          \begin{flushright}{\textit{#1}}\end{flushright}
                          \vspace*{1cm}
                          \clearpage
}
% código da monografia

\def\tipo#1{\gdef\@tipo{#1}} % tipo da monografia
\def\local#1{\gdef\@local{#1}} % local
\local{LAVRAS - MG}
\def\areaconcentracao#1{\gdef\@areaconcentracao{#1}} % área de concentracao
\def\orientador#1{\gdef\@orientador{#1}} % orientador
\def\coorientador#1{\gdef\@coorientador{#1}} % co-orientador
\coorientador{} %inicialmente vazio
\def\coorientadordois#1{\gdef\@coorientadordois{#1}} % co-orientador
\coorientadordois{} %inicialmente vazio}
\def\bancaum#1{\gdef\@bancaum{#1}} % 1. banca
\def\bancadois#1{\gdef\@bancadois{#1}} % 2. banca
\bancadois{} % inicialmente vazia
\def\bancatres#1{\gdef\@bancatres{#1}} % 2. banca
\bancatres{} % inicialmente vazia
\def\defesa#1{\gdef\@defesa{#1}} % data da defesa

% código do verso da folha de rosto - ficha catalográfica
\def\edicao#1{\gdef\@edicao{#1}} % edição
\def\fichacat#1{\gdef\@fichacat{#1}} % texto da ficha catalográfica
\edicao{}
\fichacat{}

%% dados da ficha catalográfica
% nome do curso
\def\curso#1{\gdef\@curso{#1}}
% primeiro autor
\def\fcautor#1{\gdef\@fcautor{#1}}
% autores
\def\fcautores#1{\gdef\@fcautores{#1}}
% primeiro autor
\def\fccatalogacao#1{\gdef\@fccatalogacao{#1}}
% classicacao 1
\def\fcclasi#1{\gdef\@fcclasi{#1}}
% classicacao 2
\def\fcclasii#1{\gdef\@fcclasii{#1}}
\fcclasii{}
% classicacao 3
\def\fcclasiii#1{\gdef\@fcclasiii{#1}}
\fcclasiii{}

\newcommand{\tab}{\hspace*{0.7cm}}

%\newcommand{\entrylabel}[1]{\parbox[b]{\labelwidth}{\makebox[0pt][l]{\bfseries\uppercase{#1}}\\}}

\newcommand{\hidepagenumber}{\pagestyle{empty}}
\newcommand{\showpagenumber}{\pagestyle{ufla}}

% no iní­cio, redefinimos o comando de fazer tí­tulos
% e margens


\AtBeginDocument{
\def\maketitle{    %\maketitle
%=============
% capa
%=============
\begin{titlepage}
\pagestyle{empty}
\centering

% medidas com stretch são flexí­veis (redimensionam-se para ocupar
% o espaço que sobra da página

%\vspace*{2cm}
\includegraphics[width=0.3\textwidth]{ufla}
\vspace*{11pt}

\textbf{\large\MakeUppercase\@author} % autor da monografia

%\vspace{8cm}
\vspace{\stretch{8}}

\textbf{\Large\MakeUppercase\@title} % tí­tulo da monografia

%\vspace{3cm}
\vspace{\stretch{8}}

\textbf{\large\@local\\ 
\@date}

\end{titlepage}

\newpage % terminou capa

%=============
% folha de rosto
%=============

\begin{center}
\pagestyle{empty}
% Reseta Contagem de paginas
\pagenumbering{arabic}
\centering

%\vspace*{2cm}

\textbf{\MakeUppercase\@author} % autor da monografia

%\vspace{8cm}
\vspace{\stretch{1}}

\textbf{\MakeUppercase\@title} % tí­tulo da monografia

%\vspace{3cm}
\vspace{\stretch{1}}

\hfill\begin{minipage}{0.5\textwidth}
\begin{singlespace}
\@tipo
\end{singlespace}
\end{minipage} % tipo da monografia

%\vspace{3cm}
\vspace{\stretch{1}}

%Orientador Centralizado
Orientador\\
\@orientador

\ifthenelse{\equal{\@coorientador}{}}{}{
Co-Orientador\\
\@coorientador
}

\vspace{\stretch{1}}

\textbf{\@local\\ 
\@date}

\newpage % terminou folha de rosto

%=============
% ficha catalográfica
%=============
% Ainda ser� desenvolvida.

%=============
% folha de avaliação
%=============

\textbf{\MakeUppercase\@author}

\vspace{\stretch{1}}

\textbf{\MakeUppercase\@title}

\vspace{\stretch{1}}

\hfill\begin{minipage}{0.5\textwidth}
\begin{singlespace}
\@tipo
\end{singlespace}
\end{minipage}

\vspace{\stretch{1}}

\begin{flushleft}
Aprovada em \textsl{\@defesa}\hfill \\

\vspace{\stretch{1}}

% primeira banca
\@bancaum\hfill \\

% segunda banca
\ifthenelse{\equal{\@bancadois}{\@empty}}{}   % se 2. banca nao existir, nao
                                                % produza nada
{
  \@bancadois\hfill \\
}

\ifthenelse{\equal{\@bancatres}{\@empty}}{}   % se 3. banca nao existir, nao
                                                % produza nada
{
    \@bancatres\hfill \\
}
\end{flushleft}

\vspace{\stretch{1}}

\vspace*{11pt}

\@orientador\\
Orientador

\ifthenelse{\equal{\@coorientador}{\@empty}}{}% se coorientador nao existir, 
                                              % nao produza nada
{\vspace*{11pt}

    \@coorientador \\
    Co-Orientador
}


\vspace{\stretch{1}} 

\textbf{\@local\\
\@date}

\newpage

\end{center}
}}

% Define tabulacao do paragrafo.
\setlength{\parindent}{1.25cm}

\renewcommand{\@dotsep}{1}
\newlength{\dotseplen}
\setlength{\dotseplen}{4pt}

\newlength{\defaultnumwidth}
\setlength{\defaultnumwidth}{9ex}

\def\scalenumwidth#1{\gdef\@scalenumwidth{#1}}
\scalenumwidth{1}

\renewcommand{\l@section}{\normalfont\normalsize\bfseries\@dottedtocline{1}{0ex}{\@scalenumwidth\defaultnumwidth}}
\renewcommand{\l@subsection}{\normalfont\normalsize\bfseries\@dottedtocline{2}{0ex}{\@scalenumwidth\defaultnumwidth}}
\renewcommand{\l@subsubsection}{\normalfont\normalsize\bfseries\@dottedtocline{3}{0ex}{\@scalenumwidth\defaultnumwidth}}
\renewcommand{\l@paragraph}{\normalfont\normalsize\bfseries\@dottedtocline{4}{0ex}{\@scalenumwidth\defaultnumwidth}}
\renewcommand{\l@subparagraph}{\normalfont\normalsize\bfseries\@dottedtocline{5}{0ex}{\@scalenumwidth\defaultnumwidth}}
%\renewcommand{\l@figure}{\@dottedtocline{1}{0ex}{10ex}}
%\renewcommand{\l@table}{\@dottedtocline{1}{0ex}{10ex}}

%\renewcommand{\numberline}[1]{\bfseries #1 }
%\def\figuresname{Lista de Figuras}
%\def\tablesname{Lista de Tabelas}

\setcounter{tocdepth}{5}
\setcounter{secnumdepth}{5}

\renewcommand{\tableofcontents}{
\begin{center}\textbf{\MakeUppercase{\contentsname}}\end{center}
\begin{singlespace}
 \@starttoc{toc}
\end{singlespace}
\newpage
}

\renewcommand{\listoffigures}{
\begin{center}\textbf{\MakeUppercase{\listfigurename}}\end{center}
\begin{singlespace}
 \@starttoc{lof}
\end{singlespace}
\newpage
}

\renewcommand{\listoftables}{
\begin{center}\textbf{\MakeUppercase{\listtablename}}\end{center}
\begin{singlespace}
 \@starttoc{lot}
\end{singlespace}
\newpage
}

\RequirePackage{titletoc}
\titlecontents{figure}[0pt]{\filright}{\contentspush{\begin{minipage}{10ex}\figurename\ \thecontentslabel{}\ \end{minipage}}}{}{\titlerule*[\dotseplen]{.}\contentspage}
\titlecontents{table}[0pt]{\filright}{\contentspush{\begin{minipage}{10ex}\tablename\ \thecontentslabel{}\ \end{minipage}}}{}{\titlerule*[\dotseplen]{.}\contentspage}
