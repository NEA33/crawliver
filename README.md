## crawliver

Crawling spbu.ru and msu.ru domains and collecting some stats.

Both runtime and database are presented in this repo, 
although the first one is only for small tasks. 
Database storing version is resumable, 
so you won't loose your progress if crawler is terminated.

Libs used:
- [crawle4j](https://github.com/yasserg/crawler4j) - crawling
- [c3p0](https://github.com/swaldman/c3p0) - connection pooling
- [flyway](https://flywaydb.org/) - database migrations
- [postgresql](https://www.postgresql.org/) - database
