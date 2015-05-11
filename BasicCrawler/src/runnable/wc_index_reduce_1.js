function reduce(key, counts) {
	var cnt = 0;
	// loop through call count values

	var position = [];
	//var wordsPerDoc = 0;

	for (var i = 0; i < counts.length; i++) {
		cnt = cnt + counts[i].count;
		if(counts[i].position != -1)
		position.push(counts[i].position);
	}

	return {
		count : cnt,
		position : position,
		wordsPerDoc : counts[0].wordsPerDoc,
		weight : counts[0].weight
	};

}
