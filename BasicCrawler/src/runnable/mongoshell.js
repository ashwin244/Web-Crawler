
//Aggregate: 
db.indexStorage3.aggregate([{$group: {_id: "$_id.word", total:{$sum: 1}   } }, { $out : "docsPerWord4" } ] );


//////////////////////////////////////////////////////////////////////////////
// in-links
db.inLinks.aggregate([{$group: {_id: "$inUrl", total:{$sum: 1}   } },
                      { $sort : { total : -1 } },
                      { $out : "inUrlCollection" } ] );

///////////////////////////////////////////////////////////////////////////////////////

// update query
db.indexStorage3.find().forEach(function(data) 
{
	var totalNoOfDocs = 50128;
	var docsPerWord = db.docsPerWord4.findOne({"_id":data._id.word}).total;
	var inUrl = db.inUrlCollection.findOne({"_id":data._id.fileName});
	var linkFactor;
	if(inUrl != null)
	{
		linkFactor = Math.log(inUrl.total);
	}
	else
	{
		linkFactor = 1;
	}
    db.indexStorage3.update({_id:data._id} , 
    	{ $set : {"value.tfIdf" : ( ( ( (data.value.count/data.value.wordsPerDoc) 
    	* Math.log(totalNoOfDocs/docsPerWord) ) * data.value.weight) * linkFactor ) } } ) 
} );

/////////////////////////////////////////////////////////////////////////////////////////

// export query
mongoexport -d ICS -c indexStorage1 -f _id.word,_id.fileId,value.tfIdf,value.position --csv -o C:/Users/ASHWIN/Desktop/index.txt
/////////////////////////////////////////////////////////////////////////////////////////
