<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="<?php bloginfo('html_type'); ?>; charset=<?php bloginfo('charset'); ?>" />
<title><?php if (is_home () ) { bloginfo(‘name’); }
elseif ( is_category() ) { single_cat_title(); echo ' - ' ; bloginfo(‘name’); }
elseif (is_single() ) { single_post_title();}
elseif (is_page() ) { single_post_title();}
else { wp_title(‘’,true); } ?></title>
<meta name="robots" content="index,follow" />
<meta name="generator" content="WordPress <?php bloginfo('version'); ?>" />
<meta name="description" content="rapid-framework脚手架,rapid-generator代码生成器,rapid-validation表单验证,jdbc ibatis3 ibatis分页方言(dialect),freemarker velocity jsp模板继承,插件struts struts2 springmvc hibernate ibatis spring_jdbc flex extjs " />
<meta name="keywords"  content="rapid-framework,rapid-validation,rapid-xsqlbuilder,rapid-generator,jdbc dialect,ibatis dialect,ibstis3 dialect,代码生成器" />
<link rel="stylesheet" href="<?php bloginfo('stylesheet_url'); ?>" type="text/css" media="screen" />
<link rel="alternate" type="application/rss+xml" title="<?php bloginfo('name'); ?> RSS Feed" href="<?php bloginfo('rss2_url'); ?>" />
<link rel="pingback" href="<?php bloginfo('pingback_url'); ?>" />
<link rel="icon" type="image/x-ico" href="<?php bloginfo('template_url'); ?>/images/favicon.ico" />
<?php wp_head(); ?>

</head>

<body>
<div id="wrapper">

<div id="header">
<div class="topright">
<?php include (TEMPLATEPATH . '/searchform.php'); ?>
</div> 
</div> <!-- Closes Header -->

<div class="cleared"></div>
<div id="underheader">
<div id="toprss"><a href="<?php bloginfo('rss2_url'); ?>">
<img src="<?php bloginfo('template_directory'); ?>/images/grabrss.gif" alt="grab our rss feed"></img></a>
</div>


<h1 class="sitename"><a href="<?php echo get_option('home'); ?>"><?php bloginfo('name'); ?></a></h1>
<h2 class="sitedesc"><?php bloginfo('description'); ?></h2>

</div><!-- Closes underHeader -->



<div id="nav">
<?php function get_the_pa_ges() {
  global $wpdb;
  if ( ! $these_pages = wp_cache_get('these_pages', 'pages') ) {
     $these_pages = $wpdb->get_results('select ID, post_title from '. $wpdb->posts .' where post_status = "publish" and post_type = "page" order by menu_order asc');

   }
  return $these_pages;
 }

 function list_all_pages(){

$all_pages = get_the_pa_ges ();
foreach ($all_pages as $thats_all){
$the_page_id = $thats_all->ID;

if (is_page($the_page_id)) {
  $addclass = ' class="current_page"';
  } else {
  $addclass = '';
  }
$output .= '<li' . $addclass . '><a href="'.get_permalink($thats_all->ID).'" title="'.$thats_all->post_title.'"><span>'.$thats_all->post_title.'</span></a></li>';
}

return $output;
 }
?>
<ul>
<?php

if (is_home()) {
  $addclass = ' class="current_page"';
  } else {
  $addclass = '';
  }
echo "<li" . $addclass . "><a href='" . get_option('home') . "' title='Home'><span>Home</span></a></li>";
echo list_all_pages();?>
</ul>

<div class="cleared"></div>
</div> <!-- Closes Nav -->



<div class="topcurvewhite"></div>
<div id="main">
