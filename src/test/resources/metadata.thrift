struct FileMetadata {
   1: required FormatType file_format,          // file format. ex. Parquet
   2: optional i64 record_count,                // number of records (rows)
   3: optional i64 file_size_in_bytes,          // file size in bytes
   4: optional map<i32, i64> column_sizes,      // size of columns based in column id not used in row based files.
   5: optional map<i32, i64> value_counts,      // number of values based on column id (includes null and Nan)
   6: optional map<i32, i64> null_value_counts, // number of nulls based on column id
   7: optional map<i32, i64> nan_value_counts,  // number of Nan( not a number ) based on column id
   8: optional map<i32, binary> lower_bounds,   // lower bound for each columns not include Null and NaN. binary type is a sequence of byte
   9: optional map<i32, binary> upper_bounds,   // upper bound for each columns not include Null and NaN
   10: optional binary key_metadata,            // Implementation-specific key metadata for encryption
   11: optional list<i64> split_offsets,        //Split offsets for the data file, row group offsets in a Parquet file
   12: optional i32 sort_order_id,              //ID representing sort order for this file.
}

enum FormatType {
   GENERIC,
   PARQUET,
   ORC,
   CSV
}

service MetadataService {
   void Ping(),
   FileMetadata getFileMetadata(1: string dbname, 2: string tblname, 3: string filename),
   void putFileMetadata(1: string dbname, 2: string tblname, 3: string filename, 4: FileMetadata data),
   void deleteFileMetadata(1: string dbname, 2: string tblname, 3: string filename)

   map<string, FileMetadata> getFileMetadataBatch(1: string dbname, 2: string tblname, 3: list<string> filename),
   void putFileMetadataBatch(1: string dbname, 2: string tblname, 3: list<string> filename, 4: list<FileMetadata> data),
   void deleteFileMetadataBatch(1: string dbname, 2: string tblname, 3: list<string> filenames)

}