<h1 class="code-line" data-line-start=0 data-line-end=1 ><a id="simple_lexical_analyser_for_GO_0"></a>simple_lexical_analyser_for_GO</h1>
<p class="has-line-data" data-line-start="2" data-line-end="3">Examples:</p>
<p class="has-line-data" data-line-start="4" data-line-end="5">First:</p>
<p class="has-line-data" data-line-start="6" data-line-end="11">a = 10;<br>
for(i = 0; i &lt;= 10; i++){<br>
if(i &gt; 5)<br>
a–;<br>
}</p>
<p class="has-line-data" data-line-start="12" data-line-end="13">output:</p>
<p class="has-line-data" data-line-start="14" data-line-end="42">1 Identifier a<br>
1 Operator =<br>
1 Integer literal 10<br>
1 Delimiter ;<br>
2 keyWord for<br>
2 Delimiter (<br>
2 Identifier i<br>
2 Operator =<br>
2 Integer literal 0<br>
2 Delimiter ;<br>
2 Identifier i<br>
2 Operator &lt;=<br>
2 Integer literal 10<br>
2 Delimiter ;<br>
2 Identifier i<br>
2 Operator ++<br>
2 Delimiter )<br>
2 Delimiter {<br>
3 keyWord if<br>
3 Delimiter (<br>
3 Identifier i<br>
3 Operator &gt;<br>
3 Integer literal 5<br>
3 Delimiter )<br>
4 Identifier a<br>
4 Operator –<br>
4 Delimiter ;<br>
5 Delimiter }</p>
<p class="has-line-data" data-line-start="43" data-line-end="44">Second:</p>
<p class="has-line-data" data-line-start="45" data-line-end="49">a = 228;<br>
b = 0b10001;<br>
c = 0xAFDBC;<br>
d = 2 * 2 + (4 / 2);</p>
<p class="has-line-data" data-line-start="50" data-line-end="51">output:</p>
<p class="has-line-data" data-line-start="52" data-line-end="76">1 Identifier a<br>
1 Operator =<br>
1 Integer literal 228<br>
1 Delimiter ;<br>
2 Identifier b<br>
2 Operator =<br>
2 Integer literal 0b10001<br>
2 Delimiter ;<br>
3 Identifier c<br>
3 Operator =<br>
3 Integer literal 0xAFDBC<br>
3 Delimiter ;<br>
4 Identifier d<br>
4 Operator =<br>
4 Integer literal 2<br>
4 Operator *<br>
4 Integer literal 2<br>
4 Operator +<br>
4 Delimiter (<br>
4 Integer literal 4<br>
4 Operator /<br>
4 Integer literal 2<br>
4 Delimiter )<br>
4 Delimiter ;</p>
