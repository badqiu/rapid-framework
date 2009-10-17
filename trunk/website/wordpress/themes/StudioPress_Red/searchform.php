<form method="get" id="searchform" action="<?php bloginfo('url'); ?>/">
<div>
<input type="text" value="<?php the_search_query(); ?>" name="s" id="searchbox" />
<input type="submit" id="searchbutton" value="" />
</div>
</form>