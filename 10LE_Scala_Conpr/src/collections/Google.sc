package collections

object Google {
  println("Hey Google")                           //> Hey Google

  import scala.io._
  
  val htmlWord = Set(
     "class", "div", "http", "span", "href", "www", "html", "img",
     "title", "src", "alt", "width", "height", "strong", "clearfix",
     "target", "style", "script", "type", "_blank", "item", "last",
     "text", "javascript", "middot", "point", "display", "none", "data",
     "share") //                                  //> htmlWord  : scala.collection.immutable.Set[String] = Set(span, point, style,
                                                  //|  img, data, _blank, height, div, text, strong, clearfix, last, share, alt, m
                                                  //| iddot, src, class, target, script, title, type, html, href, none, http, widt
                                                  //| h, javascript, display, www, item)
  
   val content = Source.fromURL("http://www.blick.ch")(Codec.UTF8).getLines.mkString
                                                  //> content  : String = <!DOCTYPE html><html prefix="fb: http://ogp.me/ns/fb# og
                                                  //| : http://ogp.me/ns#" lang="de" ><head>    <!-- Page hiding snippet (recommen
                                                  //| ded) -->        <style>.async-hide { opacity: 0 !important} </style>        
                                                  //| <script>            (function(a,s,y,n,c,h,i,d,e){s.className+=' '+y;h.start=
                                                  //| 1*new Date;                h.end=i=function(){s.className=s.className.replac
                                                  //| e(RegExp(' ?'+y),'')};                (a[n]=a[n]||[]).hide=h;setTimeout(func
                                                  //| tion(){i();h.end=null},c);h.timeout=c;            })(window,document.documen
                                                  //| tElement,'async-hide','dataLayer',500,                    {'GTM-57N8KPB':tru
                                                  //| e});        </script>    <script type="text/javascript">/* <![CDATA[ */var s
                                                  //| _account='ringierag-blick';/* ]]> */</script><script type="text/javascript" 
                                                  //| src="https://f.blick.ch/resources/BLICK_20170511/ver1-0/js/scz_code.js"></sc
                                                  //| ript><script type="text/javascript">/* <![CDATA[ */var _sf_startpt=(new Date
                                                  //| ()).getTime();// Chartbe
                                                  //| Output exceeds cutoff limit.
                                                  
   val words = content.split("\\W").toList        //> words  : List[String] = List("", "", DOCTYPE, html, "", html, prefix, "", fb
                                                  //| , "", http, "", "", ogp, me, ns, fb, "", og, "", http, "", "", ogp, me, ns, 
                                                  //| "", "", lang, "", de, "", "", "", head, "", "", "", "", "", "", "", "", "", 
                                                  //| Page, hiding, snippet, "", recommended, "", "", "", "", "", "", "", "", "", 
                                                  //| "", "", "", "", style, "", async, hide, "", "", opacity, "", 0, "", importan
                                                  //| t, "", "", "", style, "", "", "", "", "", "", "", "", "", script, "", "", ""
                                                  //| , "", "", "", "", "", "", "", "", "", "", function, a, s, y, n, c, h, i, d, 
                                                  //| e, "", s, className, "", "", "", "", "", y, h, start, 1, new, Date, "", "", 
                                                  //| "", "", "", "", "", "", "", "", "", "", "", "", "", "", h, end, i, function,
                                                  //|  "", "", s, className, s, className, replace, RegExp, "", "", "", "", "", y,
                                                  //|  "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                                                  //|  "", "", "", "", a, n, "", a, n, "", "", "", "", "", "", hide, h, setTimeout
                                                  //| , function, "", "", i, "
                                                  //| Output exceeds cutoff limit.
   
     
   
   val important = words
     .filter(w => w.size > 3)
     .filter(w => !htmlWord(w))                   //> important  : List[String] = List(DOCTYPE, prefix, lang, head, Page, hiding, 
                                                  //| snippet, recommended, async, hide, opacity, important, function, className, 
                                                  //| start, Date, function, className, className, replace, RegExp, hide, setTimeo
                                                  //| ut, function, null, timeout, window, document, documentElement, async, hide,
                                                  //|  dataLayer, 57N8KPB, true, CDATA, s_account, ringierag, blick, https, blick,
                                                  //|  resources, BLICK_20170511, ver1, scz_code, CDATA, _sf_startpt, Date, getTim
                                                  //| e, Chartbeatvar, WEMF_section, home, special, ressort, document, addEventLis
                                                  //| tener, DOMContentLoaded, function, typeof, dataLayer, undefined, dataLayer, 
                                                  //| push, event, wemfSection, Pageview, function, getWEMF, caller, typeof, dataL
                                                  //| ayer, undefined, dataLayer, push, event, wemfSection, Pageview, function, se
                                                  //| tDefaultProperties, category, agof, swipe, delomni, prop4, Nachrichten, Schw
                                                  //| eiz, aller, Welt, Blick, pageName, home, channel, home, category, prop6, cat
                                                  //| egory, else, prop6, ress
                                                  //| Output exceeds cutoff limit.

   val grouped = important.groupBy(w => w)        //> grouped  : scala.collection.immutable.Map[String,List[String]] = Map(id43186
                                                  //| 90 -> List(id4318690), Milch -> List(Milch), zerstoert -> List(zerstoert, ze
                                                  //| rstoert, zerstoert, zerstoert, zerstoert, zerstoert, zerstoert), cname -> Li
                                                  //| st(cname, cname, cname, cname, cname), 7754531779 -> List(7754531779), Terro
                                                  //| r -> List(Terror), prop17 -> List(prop17), krebs -> List(krebs, krebs, krebs
                                                  //| , krebs, krebs, krebs, krebs), multis -> List(multis, multis, multis, multis
                                                  //| , multis, multis, multis, multis, multis), alpha -> List(alpha, alpha, alpha
                                                  //| , alpha, alpha, alpha, alpha, alpha, alpha, alpha, alpha, alpha, alpha, alph
                                                  //| a, alpha), Bolero -> List(Bolero, Bolero, Bolero, Bolero, Bolero), sechs -> 
                                                  //| List(sechs, sechs, sechs, sechs, sechs, sechs, sechs, sechs, sechs, sechs, s
                                                  //| echs, sechs, sechs), Motoren -> List(Motoren, Motoren, Motoren, Motoren, Mot
                                                  //| oren), 5335573057 -> List(5335573057), essen -> List(essen, essen, essen, es
                                                  //| sen, essen, essen, essen
                                                  //| Output exceeds cutoff limit.
   val list = grouped.mapValues(v => v.size).toList
                                                  //> list  : List[(String, Int)] = List((id4318690,1), (Milch,1), (zerstoert,7), 
                                                  //| (cname,5), (7754531779,1), (Terror,1), (prop17,1), (krebs,7), (multis,9), (a
                                                  //| lpha,15), (Bolero,5), (sechs,13), (Motoren,5), (5335573057,1), (essen,23), (
                                                  //| article1740258,1), (autoCenter,1), (nbsp,2), (id6655044,7), (RC120F477910,1)
                                                  //| , (Einbrecher,8), (static,1), (Menge,4), (Tagesticker,5), (shareWithFBIMG,10
                                                  //| 2), (patrona,7), (sweet,4), (id4317648,1), (topteaser,32), (Lesen,2), (cupsi
                                                  //| eger,1), (wemfSection,3), (blick_tile,1), (6685562,6), (Laws,5), (Baumhaus,5
                                                  //| ), (sentiert,3), (Dreh,5), (Lehrer,12), (Anriss,1), (mutter,14), (Meldungen,
                                                  //| 3), (Football,3), (Weinflasche,6), (crop6723094,1), (userDataLoged,1), (Kreu
                                                  //| zwortr,8), (leuppi,7), (Vettel100,1), (tbody,6), (fashion,12), (ariana,7), (
                                                  //| 6725114,7), (Zeiten,7), (id6733174,7), (teaser__tabs_fwid58,3), (streit,8), 
                                                  //| (Pearl,1), (preventDefault,12), (praesident,15), (id2554794,1), (8214539070,
                                                  //| 1), (fwid10,2), (addClas
                                                  //| Output exceeds cutoff limit.
   val top10 = list.sortBy(p => p._2).reverse.take(10)
                                                  //> top10  : List[(String, Int)] = List((blick,1197), (catchword,395), (news,355
                                                  //| ), (onclick,328), (utm_medium,315), (utm_campaign,315), (menu,315), (utm_sou
                                                  //| rce,315), (blick_web,307), (social_user,306))
   
}