Some notes on the implementation:

* As this is a rapid dev scenario, proper structure including DI and UI/logic separation such as MVP or MVVM has not 
  been implemented
* It has been assumed that only European countries should be included as Niue is a bit of an outlier and the 
  instructions made reference to "European countries"
* It is noted that in the provided json data nearly every capital city is located in the wrong place, including 
  London being somewhere in the Yorkshire Dales. Fixing this was deemed to be out of scope.
* Ideally the json parsing should lead to vars in camelCase instead of snake_case but I had some difficulty getting 
  Moshi serialisation annotations working, so I left it.
  
With regards to Part 3 (journey planner):
Some further definition would be required to properly implement this, including the following questions:
1. Should they be any 4 countries or should the user be able to select which countries to travel to? Perhaps they 
   should just be the four countries adjacent to the home country?
2. What mode of transport is assumed? This is fairly simple as the crow flies but for ground transport you'd 
   probably want to make use of a tool like Google's 
   [Distance Matrix API](https://developers.google.com/maps/documentation/distance-matrix/overview).

Once the real distances are established, this problem would suggest some form of Dijkstra's algorithm to be 
implemented. This broadly involves finding the nearest unvisited city and adding that to the path. After 3 stops, we 
would then return to the origin. This might however involve getting further away from the origin than necessary so as a 
preliminary step we might restrict the choice of cities to the 4 capital cities closest to the origin.