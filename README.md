# DataLake
*Data normalization, storage and search platform.*

The DataLake project is a data normalization, storage and search platform. It attempts to normalize and find meaningful relationships between large data sets of varying formats.


*How it works:*

1. Accepts datasources of various formats such as .json, .xml, and .csv. These can be uploaded via the web interface.
2. Extractor is triggered and run on uploaded data. Extractor attempts to create a tree to represent the data and then breaks the tree into components for storage in relational databases.
3. Extractor maintains an inverted index connecting all words that have been found in processed data to the nodes that contained them.
..* For example, a basic json containing a single user's profile information such as {"name":"Ryan", "occupation":"SuperDev"} will be assigned a root node with the doc title and then have two children nodes. Entries will be created in the index connecting "name" and "Ryan" with the first child node, and "occupation" and "SuperDev" with the second.
4. A linker is run periodically in the background. It evaluates all pairs of nodes in the databases for certain relationships. The most basic of these is string equality. Upon indentifying two related nodes, linker edges are created in the database.
5. The data can be searched via the web interface. Single and Two Word Search are offered and work as follows:
..* Single Word Search: Indentifies nodes containing the search terms. Fetches all nodes and edges for the containing documents and recreates each representing the document as a tree. Starts at the root and returns the shortest path from root to target node for each document.
..* Two Word Search: Identifies all nodes containing the search terms. Fetches all nodes and edges for all containing documents, then fetches all linker-generated edges between nodes in those documents. Resulting struture is a graph represented internally as an adjacency matrix. Runs BFS starting at each of the start nodes (those pertaining to search term one) and continues until reaching any of the target nodes (those pertaining to search term two). Returns all paths between the start and target nodes of depth <= 25.
6. Results are displayed in graph form and relevant documents can be downloaded for further interrogation.
7. Users can create accounts and enforce permissions over documents. These are currently limited to 'Public', 'Private' and 'Elevated' which would pertain to a user's affiliated group (e.g. Penn CIS 550). Permissions could be extended to allow granting permission to individual users. Search and download respect permissions.


*Implementation Details*

* MySQL databases for normalized data and inverted index storage.
* S3 for raw document storage and downloads.
* ActiveMQ message queue to trigger extractor when new document uploaded.
* Spring framework and Jdbc templates for handrolled ORM of sorts.
* Spring MVC and Tomcat for web.
* Maven for build.


A demonstration video can be seen here: https://www.youtube.com/watch?v=FZQxBHsoJpA