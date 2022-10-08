# PROPOE
PROPOE - Prose to Poem, sistema de geração automática de poemas a partir da prosa literária brasileira

O sistema PROPOE (Prose to Poem) é o primeiro sistema para geração de poemas do português brasileiro. É também o primeiro sistema computacional que produz poemas a partir de sentenças métricas advindas da prosa literária. PROPOE gera computacionalmente poemas versificados em língua portuguesa a partir de sentenças versificadas (estruturas heterométricas) fornecidas pelo MIVES (Mining Verse Structure), que extrai, identifica e classifica estruturas de obras da prosa literária de língua portuguesa, particularmente brasileira. 

O sistema (PROPOE) realiza combinações das sentenças, e gera poemas baseados na otimização de critérios rítmicos e fonológicos. É aplicado um “algoritmo guloso” (greedy algorithm) no qual são consideradas normas rítmicas e rimas estabelecidas para o português. Em uma etapa final, é realizada uma avaliação automatizada, e atribuição de pontuação de acordo com a identificação de algum padrão considerado ideal em poemas com métricas regulares, relacionado a rima identificada nas palavras que compõem os versos, bem como o ritmo.

Este repositório contém o código fonte do sistema. 

Confira versão executável em JAR (Java) em https://github.com/lasicuefs/propoe/releases.

Veja mais sobre o PROPOE em https://propoetool.wordpress.com/
