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


% identificacao
\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{uflamon}[2007/09/11]

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
\LoadClass[11pt,letter,oneside]{book} % classe base para a monografia
\RequirePackage{ifthen} % para testes
\RequirePackage{calc} % para cí¡lculos (margens, etc.) 
\RequirePackage{lastpage} % para ficha catalográfica

% Definção de margens
\setlength{\hoffset}{-1in} % remove padrão do LaTeX 
\setlength{\voffset}{-1in} % remove padrão do LaTeX 
\setlength{\oddsidemargin}{4.5cm}  % margem esquerda
\setlength{\evensidemargin}{4.5cm} % margem direita
\setlength{\topmargin}{4.5cm}    % margem superior
\newlength{\bottommargin}        % margem inferior é deduzida em LaTeX
\setlength{\bottommargin}{4.5cm} % margem inferior
\setlength{\headheight}{0pt} % altura do cabeçalho
\setlength{\headsep}{0pt}    % espaço entre cabeçalho e texto
\setlength{\footskip}{12mm}  % espaço entre rodapé e texto
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
\fancyfoot[C]{\thepage}
\renewcommand{\headrulewidth}{0pt}
\renewcommand{\footrulewidth}{0pt}
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
                          \begin{center}\Large{\textbf{Agradecimentos}}\end{center}
                          \begin{quote}#1\end{quote}
                          \vspace*{1cm}
                          \vfill\null
                          \clearpage
                         }

\def\palchaves#1{\gdef\@palchaves{\nh{#1}}} % palavras-chave da monografia
\palchaves{} %inicialmente vazio
\def\keywords#1{\gdef\@keywords{\nh{#1}}} % palavras-chave da monografia (em inglês)
\keywords{} %inicialmente vazio

\newcommand{\abstract}[2]{
                          \clearpage
                          \null\vfill
                          \begin{center}\Large{\textbf{Resumo}}\end{center}
                          \begin{quote}#1\end{quote}
						  \ifthenelse{\equal{\@palchaves}{\nh{}}}{}{
                               \begin{quote}{\bfseries Palavras-Chave:} \@palchaves \end{quote}   
                          }                       

                          \vspace*{3.5cm}

                          \begin{center}\Large{\textbf{Abstract}}\end{center}
                          \begin{quote}#2\end{quote}
						  \ifthenelse{\equal{\@keywords}{\nh{}}}{}{
                               \begin{quote}{\bfseries Keywords:} \@keywords \end{quote}   
                          }                       
						  \vfill\null
                          \clearpage
                         }

\newcommand{\dedic}[1]{
                          \clearpage
                          \null\vfill
                          \begin{flushright}{\textit{#1}}\end{flushright}
                          \vspace*{1cm}
                          \clearpage
                         }

% definindo comando para aparecer numeração romana, apenas criei novo comando
% chapterExt com estilo de página normal e redefini os comandos de listas
%  para utilizarem esses comandos ao invés do chapter normal.
\newcommand\chapterExt{\if@openright\cleardoublepage\else\clearpage\fi
                    \thispagestyle{ufla}%
                    \global\@topnum\z@
                    \@afterindentfalse
                    \secdef\@chapter\@schapter}
 

\renewcommand\listoftables{%
    \if@twocolumn
      \@restonecoltrue\onecolumn
    \else
      \@restonecolfalse
    \fi
    \chapterExt*{\listtablename}%
      \@mkboth{%
          \MakeUppercase\listtablename}%
         {\MakeUppercase\listtablename}%
    \@starttoc{lot}%
    \if@restonecol\twocolumn\fi
    }


\renewcommand\listoffigures{%
    \if@twocolumn
      \@restonecoltrue\onecolumn
    \else
      \@restonecolfalse
    \fi
    \chapterExt*{\listfigurename}%
      \@mkboth{\MakeUppercase\listfigurename}%
              {\MakeUppercase\listfigurename}%
    \@starttoc{lof}%
    \if@restonecol\twocolumn\fi
    }

\renewcommand\tableofcontents{%
    \if@twocolumn
      \@restonecoltrue\onecolumn
    \else
      \@restonecolfalse
    \fi
    \chapterExt*{\contentsname
        \@mkboth{%
           \MakeUppercase\contentsname}{\MakeUppercase\contentsname}}%
    \@starttoc{toc}%
    \if@restonecol\twocolumn\fi
    }


% código da monografia

\def\tipo#1{\gdef\@tipo{\nh{#1}}} % tipo da monografia
\def\local#1{\gdef\@local{#1}} % local
\local{LAVRAS \\ MINAS GERAIS -- BRASIL}
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

%\fichacat{% texto da ficha catalográfica
% \renewcommand{\baselinestretch}{1}\normalsize %espaçamento
%  \@fcautor \\ \\
%  \tab \@title / \@fcautores. 
%  \ifthenelse{\equal{\@edicao}{\@empty}}{}{\@edicao.ed.} 
%  Lavras -- Minas Gerais, 2004. \pageref{LastPage} p. : il. \\ 
%  \\
%  \tab Monografia de Gradua\c{c}\~ao -- Universidade Federal de Lavras. Departamento de Ci\^encia da 
%Computa\c{c}\~ao.\\
%  \\
%  \tab \@fccatalogacao\\
%  \ifthenelse{\equal{\@fcclasii}{\@empty}}{}{
%  \\
%  \tab \hfill 
%  \begin{tabular}{r@{-}l} 
%	CDD & \@fcclasi 
%  \ifthenelse{\equal{\@fcclasii}{\@empty}}{}{
%  \\ & \@fcclasii 
%  }
%  \ifthenelse{\equal{\@fcclasiii}{\@empty}}{}{
%  \\ & \@fcclasiii 
%  }
%  \end{tabular}
%  \tab
%  }
%}


%\newcommand{\entrylabel}[1]{\parbox[b]{\labelwidth}{\makebox[0pt][l]{\bfseries\uppercase{#1}}\\}}


% no iní­cio, redefinimos o comando de fazer tí­tulos
% e margens


\AtBeginDocument{
\def\maketitle{    %\maketitle

\begin{titlepage}
%=============
% capa
%=============
\pagestyle{empty}
\pagenumbering{roman}
\centering

% medidas com stretch são flexí­veis (redimensionam-se para ocupar
% o espaço que sobra da página

%\vspace*{2cm}
\vspace*{\stretch{2}}

\textbf{\MakeUppercase\@author} % autor da monografia

%\vspace{8cm}
\vspace{\stretch{8}}

\textbf{\MakeUppercase\@title} % tí­tulo da monografia

%\vspace{3cm}
\vspace{\stretch{3}}

\hfill\begin{minipage}{9.5cm}{\@tipo}\end{minipage} % tipo da monografia

%\vspace{3cm}
\vspace{\stretch{3}}

\@local\\ 
\@date

\newpage % terminou capa

%=============
% folha de rosto
%=============

\pagestyle{empty}
\centering

%\vspace*{2cm}
\vspace*{\stretch{2}}

\textbf{\MakeUppercase\@author} % autor da monografia

%\vspace{8cm}
\vspace{\stretch{3.5}}

\textbf{\MakeUppercase\@title} % tí­tulo da monografia

%\vspace{3cm}
\vspace{\stretch{3.5}}

\hfill\begin{minipage}{9.5cm}{\@tipo}\end{minipage} % tipo da monografia

%\vspace{3cm}
\vspace{\stretch{2}}

\hfill\begin{minipage}{9.5cm}% área e orientador
\'Area de Concentra\c{c}\~ao: \\
\@areaconcentracao\\

Orientador:\\
\@orientador
\end{minipage} 

\vspace{\stretch{2}}

\@local\\ 
\@date

\newpage % terminou folha de rosto

%=============
% ficha catalográfica
%=============

%
%\vspace*{\stretch{12}}
%
%\begin{center}
%      \bfseries
%      Ficha Catalogr\'afica preparada pela Divis\~ao de Processos T\'ecnicos \\
%      da Biblioteca Central da UFLA
%\end{center}
%
%\vspace{-0.3cm}

\newlength{\mylen}
\addtolength{\mylen}{\textwidth}
\addtolength{\mylen}{-0.8cm}
\newlength{\mylenn}
\addtolength{\mylenn}{\textwidth}
\addtolength{\mylenn}{-1.2cm}

%\noindent \hfill  \fbox{\parbox[c]{\mylen}{
%        \vspace{0.2cm}
%        \hfill \parbox[c]{\mylenn}{
%         % \@fichacat
%          } \hfill
%        \vspace{0.1cm}
%        }
%} \hfill

%\vspace{\stretch{2}}
%
%\newpage


%=============
% folha de avaliação
%=============

\textbf{\MakeUppercase\@author}

\vspace{\stretch{2.5}}

\textbf{\MakeUppercase\@title}

\vspace{\stretch{2.5}}

\hfill\begin{minipage}{9.5cm}{%
\@tipo}%
\end{minipage}

\vspace{\stretch{2}}

Aprovada em \textsl{\@defesa} \\

\vspace{\stretch{2}}

% primeira banca
\setlength{\mylen}{8cm}
\rule{\mylen}{1pt}\\
 \@bancaum\\

% segunda banca
\ifthenelse{\equal{\@bancadois}{\@empty}}{}   % se 2. banca nao existir, nao
                                                % produza nada
{\vspace{\stretch{.4}}                        % caso contrario, escreva-a
  \rule{\mylen}{1pt}\\
  \@bancadois\\
}

\ifthenelse{\equal{\@bancatres}{\@empty}}{}   % se 3. banca nao existir, nao
                                                % produza nada
  {\vspace{\stretch{.4}}                        % caso contrario, escreva-a
    \rule{\mylen}{1pt}\\
    \@bancatres\\
}


\vspace{\stretch{.4}}

\rule{\mylen}{1pt}\\
\@orientador\\
(Orientador)

\ifthenelse{\equal{\@coorientador}{\@empty}}{}% se coorientador nao existir, 
                                              % nao produza nada
{\vspace{\stretch{.4}}                        % caso contrario, escreva-o
     \rule{\mylen}{1pt}\\
    \@coorientador \\
    (Co-Orientador)
}

\ifthenelse{\equal{\@coorientadordois}{\@empty}}{}% se coorientador nao existir, 
                                              % nao produza nada
{\vspace{\stretch{.4}}                        % caso contrario, escreva-o
     \rule{\mylen}{1pt}\\
    \@coorientadordois\\
    (Co-Orientador)
}


\vspace{\stretch{2}} 

{\@local}\\
\@date

\newpage
\null

\end{titlepage}
\pagestyle{plain}
}}
