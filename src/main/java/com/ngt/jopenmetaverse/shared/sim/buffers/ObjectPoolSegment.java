package com.ngt.jopenmetaverse.shared.sim.buffers;

public class ObjectPoolSegment 
{
//	private Queue<T> _liveInstances = new Queue<T>();
//    private int _segmentNumber;
//    private int _originalCount;
//    private bool _isDisposed = false;
//    private DateTime _eligibleForDeletionAt;
//
//    public int SegmentNumber { get { return _segmentNumber; } }
//    public int AvailableItems { get { return _liveInstances.Count; } }
//    public DateTime DateEligibleForDeletion { get { return _eligibleForDeletionAt; } }
//
//    public ObjectPoolSegment(int segmentNumber, Queue<T> liveInstances, DateTime eligibleForDeletionAt)
//    {
//        _segmentNumber = segmentNumber;
//        _liveInstances = liveInstances;
//        _originalCount = liveInstances.Count;
//        _eligibleForDeletionAt = eligibleForDeletionAt;
//    }
//
//    public bool CanBeCleanedUp()
//    {
//        if (_isDisposed == true)
//            throw new ObjectDisposedException("ObjectPoolSegment");
//
//        return ((_originalCount == _liveInstances.Count) && (DateTime.Now > _eligibleForDeletionAt));
//    }
//
//    public void Dispose()
//    {
//        if (_isDisposed)
//            return;
//
//        _isDisposed = true;
//
//        bool shouldDispose = (typeof(T) is IDisposable);
//        while (_liveInstances.Count != 0)
//        {
//            T instance = _liveInstances.Dequeue();
//            if (shouldDispose)
//            {
//                try
//                {
//                    (instance as IDisposable).Dispose();
//                }
//                catch (Exception) { }
//            }
//        }
//    }
//
//    internal void CheckInObject(T o)
//    {
//        if (_isDisposed == true)
//            throw new ObjectDisposedException("ObjectPoolSegment");
//
//        _liveInstances.Enqueue(o);
//    }
//
//    internal T CheckOutObject()
//    {
//        if (_isDisposed == true)
//            throw new ObjectDisposedException("ObjectPoolSegment");
//
//        if (0 == _liveInstances.Count)
//            throw new InvalidOperationException("No Objects Available for Checkout");
//
//        T o = _liveInstances.Dequeue();
//        return o;
//    }
}
