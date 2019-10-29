-- total sums
SELECT sum(bytes_length) as total_bytes,
       sum(total_links) as total_links,
       sum(external_links) as total_external_links,
       sum(text_size) as total_text_size,
       sum(html_size) as total_html_size
FROM public.page;

-- broken
SELECT id, url FROM page WHERE status_code <> 200;

-- domain names
SELECT DISTINCT substring(temp_str, 0, position('/' in temp_str)) AS domain_name FROM
    (SELECT substring(url, position('//' in url) + 2) AS temp_str FROM page) AS temp_view;

-- almost domain
SELECT substring(url from '//(.+)/|$') FROM page;

-- sum grouped by domain
SELECT domain_name,
       sum(bytes_length) as total_bytes,
       sum(total_links) as total_links,
       sum(external_links) as total_external_links,
       sum(text_size) as total_text_size,
       sum(html_size) as total_html_size
FROM
    (SELECT bytes_length,
            total_links,
            external_links,
            text_size,
            html_size,
            substring(url from '//(.+)/|$') AS domain_name
     FROM page) AS main_stats
GROUP BY domain_name
ORDER BY domain_name;

-- doc types
SELECT type, COUNT(type) FROM page GROUP BY type;
