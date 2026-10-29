package com.jonwal.dancetempo.data.local;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.collection.LongSparseArray;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomDatabaseKt;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.RelationUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.jonwal.dancetempo.data.local.entity.SongDanceCrossRef;
import com.jonwal.dancetempo.data.local.entity.SongEntity;
import com.jonwal.dancetempo.data.local.entity.SongWithDances;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SongDao_Impl implements SongDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SongEntity> __insertionAdapterOfSongEntity;

  private final EntityInsertionAdapter<SongDanceCrossRef> __insertionAdapterOfSongDanceCrossRef;

  private final EntityDeletionOrUpdateAdapter<SongEntity> __deletionAdapterOfSongEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteSongById;

  public SongDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSongEntity = new EntityInsertionAdapter<SongEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `songs` (`id`,`title`,`artist`,`bpm`,`meter`,`rating`,`notes`,`streaming_url`,`created_at`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SongEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTitle());
        statement.bindString(3, entity.getArtist());
        statement.bindLong(4, entity.getBpm());
        statement.bindLong(5, entity.getMeter());
        statement.bindLong(6, entity.getRating());
        if (entity.getNotes() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getNotes());
        }
        if (entity.getStreamingUrl() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getStreamingUrl());
        }
        statement.bindLong(9, entity.getCreatedAt());
      }
    };
    this.__insertionAdapterOfSongDanceCrossRef = new EntityInsertionAdapter<SongDanceCrossRef>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR IGNORE INTO `song_dance_cross_ref` (`song_id`,`dance_id`) VALUES (?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SongDanceCrossRef entity) {
        statement.bindLong(1, entity.getSongId());
        statement.bindString(2, entity.getDanceId());
      }
    };
    this.__deletionAdapterOfSongEntity = new EntityDeletionOrUpdateAdapter<SongEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `songs` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SongEntity entity) {
        statement.bindLong(1, entity.getId());
      }
    };
    this.__preparedStmtOfDeleteSongById = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM songs WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSong(final SongEntity song, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSongEntity.insertAndReturnId(song);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSongDanceCrossRefs(final List<SongDanceCrossRef> crossRefs,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSongDanceCrossRef.insert(crossRefs);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteSong(final SongEntity song, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSongEntity.handle(song);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertSongWithDances(final SongEntity song, final List<String> danceIds,
      final Continuation<? super Long> $completion) {
    return RoomDatabaseKt.withTransaction(__db, (__cont) -> SongDao.DefaultImpls.insertSongWithDances(SongDao_Impl.this, song, danceIds, __cont), $completion);
  }

  @Override
  public Object deleteSongById(final long songId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteSongById.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, songId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteSongById.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SongWithDances>> getAllSongsWithDances() {
    final String _sql = "SELECT * FROM songs ORDER BY created_at DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, true, new String[] {"song_dance_cross_ref",
        "songs"}, new Callable<List<SongWithDances>>() {
      @Override
      @NonNull
      public List<SongWithDances> call() throws Exception {
        __db.beginTransaction();
        try {
          final Cursor _cursor = DBUtil.query(__db, _statement, true, null);
          try {
            final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
            final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
            final int _cursorIndexOfArtist = CursorUtil.getColumnIndexOrThrow(_cursor, "artist");
            final int _cursorIndexOfBpm = CursorUtil.getColumnIndexOrThrow(_cursor, "bpm");
            final int _cursorIndexOfMeter = CursorUtil.getColumnIndexOrThrow(_cursor, "meter");
            final int _cursorIndexOfRating = CursorUtil.getColumnIndexOrThrow(_cursor, "rating");
            final int _cursorIndexOfNotes = CursorUtil.getColumnIndexOrThrow(_cursor, "notes");
            final int _cursorIndexOfStreamingUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "streaming_url");
            final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "created_at");
            final LongSparseArray<ArrayList<SongDanceCrossRef>> _collectionDanceRefs = new LongSparseArray<ArrayList<SongDanceCrossRef>>();
            while (_cursor.moveToNext()) {
              final long _tmpKey;
              _tmpKey = _cursor.getLong(_cursorIndexOfId);
              if (!_collectionDanceRefs.containsKey(_tmpKey)) {
                _collectionDanceRefs.put(_tmpKey, new ArrayList<SongDanceCrossRef>());
              }
            }
            _cursor.moveToPosition(-1);
            __fetchRelationshipsongDanceCrossRefAscomJonwalDancetempoDataLocalEntitySongDanceCrossRef(_collectionDanceRefs);
            final List<SongWithDances> _result = new ArrayList<SongWithDances>(_cursor.getCount());
            while (_cursor.moveToNext()) {
              final SongWithDances _item;
              final SongEntity _tmpSong;
              final long _tmpId;
              _tmpId = _cursor.getLong(_cursorIndexOfId);
              final String _tmpTitle;
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
              final String _tmpArtist;
              _tmpArtist = _cursor.getString(_cursorIndexOfArtist);
              final int _tmpBpm;
              _tmpBpm = _cursor.getInt(_cursorIndexOfBpm);
              final int _tmpMeter;
              _tmpMeter = _cursor.getInt(_cursorIndexOfMeter);
              final int _tmpRating;
              _tmpRating = _cursor.getInt(_cursorIndexOfRating);
              final String _tmpNotes;
              if (_cursor.isNull(_cursorIndexOfNotes)) {
                _tmpNotes = null;
              } else {
                _tmpNotes = _cursor.getString(_cursorIndexOfNotes);
              }
              final String _tmpStreamingUrl;
              if (_cursor.isNull(_cursorIndexOfStreamingUrl)) {
                _tmpStreamingUrl = null;
              } else {
                _tmpStreamingUrl = _cursor.getString(_cursorIndexOfStreamingUrl);
              }
              final long _tmpCreatedAt;
              _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
              _tmpSong = new SongEntity(_tmpId,_tmpTitle,_tmpArtist,_tmpBpm,_tmpMeter,_tmpRating,_tmpNotes,_tmpStreamingUrl,_tmpCreatedAt);
              final ArrayList<SongDanceCrossRef> _tmpDanceRefsCollection;
              final long _tmpKey_1;
              _tmpKey_1 = _cursor.getLong(_cursorIndexOfId);
              _tmpDanceRefsCollection = _collectionDanceRefs.get(_tmpKey_1);
              _item = new SongWithDances(_tmpSong,_tmpDanceRefsCollection);
              _result.add(_item);
            }
            __db.setTransactionSuccessful();
            return _result;
          } finally {
            _cursor.close();
          }
        } finally {
          __db.endTransaction();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private void __fetchRelationshipsongDanceCrossRefAscomJonwalDancetempoDataLocalEntitySongDanceCrossRef(
      @NonNull final LongSparseArray<ArrayList<SongDanceCrossRef>> _map) {
    if (_map.isEmpty()) {
      return;
    }
    if (_map.size() > RoomDatabase.MAX_BIND_PARAMETER_CNT) {
      RelationUtil.recursiveFetchLongSparseArray(_map, true, (map) -> {
        __fetchRelationshipsongDanceCrossRefAscomJonwalDancetempoDataLocalEntitySongDanceCrossRef(map);
        return Unit.INSTANCE;
      });
      return;
    }
    final StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT `song_id`,`dance_id` FROM `song_dance_cross_ref` WHERE `song_id` IN (");
    final int _inputSize = _map.size();
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _stmt = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int i = 0; i < _map.size(); i++) {
      final long _item = _map.keyAt(i);
      _stmt.bindLong(_argIndex, _item);
      _argIndex++;
    }
    final Cursor _cursor = DBUtil.query(__db, _stmt, false, null);
    try {
      final int _itemKeyIndex = CursorUtil.getColumnIndex(_cursor, "song_id");
      if (_itemKeyIndex == -1) {
        return;
      }
      final int _cursorIndexOfSongId = 0;
      final int _cursorIndexOfDanceId = 1;
      while (_cursor.moveToNext()) {
        final long _tmpKey;
        _tmpKey = _cursor.getLong(_itemKeyIndex);
        final ArrayList<SongDanceCrossRef> _tmpRelation = _map.get(_tmpKey);
        if (_tmpRelation != null) {
          final SongDanceCrossRef _item_1;
          final long _tmpSongId;
          _tmpSongId = _cursor.getLong(_cursorIndexOfSongId);
          final String _tmpDanceId;
          _tmpDanceId = _cursor.getString(_cursorIndexOfDanceId);
          _item_1 = new SongDanceCrossRef(_tmpSongId,_tmpDanceId);
          _tmpRelation.add(_item_1);
        }
      }
    } finally {
      _cursor.close();
    }
  }
}
