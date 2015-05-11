function map() {
	// get content of the current document
	var title = this.title;
	var text = this.text;
	var url = this.url;
	// split the content into an array of words using a regular expression

	var words = text.split(" ");
	var titleArr = title.split(" ");

	// if there are no words, return
	if (words == null) {
		return;
	}
	
	

		for (var i = 0; i < titleArr.length; i++) {
		if (!isStopWord(titleArr[i]) && titleArr[i].length > 1
				&& titleArr[i].length < 128) {

			if (url.indexOf("feed") ==-1) {
				emit({
					word : titleArr[i],
					fileName : url,
				}, {
					count : 1,
					position : -1,
					wordsPerDoc : words.length,
					weight : 100
				});
			}
			
			else {
				emit({
					word : titleArr[i],
					fileName : url,
				}, {
					count : 1,
					position : -1,
					wordsPerDoc : words.length,
					weight : 1
				});
			}
		}
	}
	
	for (var i = 0; i < words.length; i++) 
	{
		if (!isStopWord(words[i]) && words[i].length > 1 && words[i].length < 128) {
			emit({
				word : words[i],
				fileName : url,
			}, {
				count : 1,
				position : i,
				wordsPerDoc : words.length,
				weight : 1
			});
		}
	}
}

//Source - https://github.com/mirzaasif/JS-StopWord/blob/master/StopWord.js
function isStopWord(word) {
	var regex = new RegExp("\\b" + word + "\\b", "i");

	if (stop_words.search(regex) < 0) {
		return false;
	} else {
		return true;
	}
}

var stop_words = "a, about, above, after, again, against, all, am, an, and, any, are, aren't, as, at, be, because, been, before, being, below, between, both,\
	but, by, can't, cannot, could, couldn't, did, didn't, do, does, doesn't, doing, don't, down, during, each, few, for, from, further, had, hadn't, has, hasn't, have,\
	haven't, having, he, he'd, he'll, he's, her, here, here's, hers, herself, him, himself, his, how, how's, i, i'd, i'll, i'm, i've, if, in, into, is, isn't, it, it's, its,\
	itself, let's, me, more, most, mustn't, my, myself, no, nor, not, of, off, on, once, only, or, other, ought, our, ours, ourselves, out, over, own, same, shan't, she, she'd,\
	she'll, she's, should, shouldn't, so, some, such, than, that, that's, the, their, theirs, them, themselves, then, there, there's, these, they, they'd, they'll, they're, they've,\
	this, those, through, to, too, under, until, up, very, was, wasn't, we, we'd, we'll, we're, we've, were, weren't, what, what's, when, when's, where, where's, which, while, who,\
	who's, whom, why, why's, with, won't, would, wouldn't, you, you'd, you'll, you're, you've, your, yours, yourself, yourselves";
